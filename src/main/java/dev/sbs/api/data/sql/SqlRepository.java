package dev.sbs.api.data.sql;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.sql.exception.SqlException;
import dev.sbs.api.data.sql.function.FilterFunction;
import dev.sbs.api.data.sql.function.ReturnSessionFunction;
import dev.sbs.api.util.concurrent.Concurrent;
import dev.sbs.api.util.concurrent.ConcurrentList;
import dev.sbs.api.util.helper.ListUtil;
import dev.sbs.api.util.tuple.Pair;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static dev.sbs.api.util.helper.TimeUtil.ONE_MINUTE_MS;

@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public abstract class SqlRepository<T extends SqlModel> {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(0);
    private static final Object schedulerLock = new Object();

    private final Object initLock = new Object();
    private final Class<T> tClass;
    private final SqlSession sqlSession;
    private Boolean itemsInitialized = false;
    private ConcurrentList<T> items;
    @Getter private long lastRefreshed;
    @Getter private long refreshDuration;

    /**
     * Creates a new repository of type {@link T}.
     * Defaults to autocaching every 1 minute.
     *
     * @param sqlSession the sql session to use
     */
    public SqlRepository(@NonNull SqlSession sqlSession) {
        this(sqlSession, ONE_MINUTE_MS);
    }

    /**
     * Creates a new repository of type {@link T}.
     * Refreshes cache every {@param fixedUpdateRateMs} milliseconds.
     *
     * @param sqlSession the sql session to use
     * @param fixedUpdateRateMs how many milliseconds to wait between refreshing the cache
     */
    @SuppressWarnings("unchecked")
    public SqlRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        this.sqlSession = sqlSession;
        ParameterizedType superClass = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.tClass = (Class<T>) superClass.getActualTypeArguments()[0];

        synchronized (schedulerLock) {
            scheduler.scheduleAtFixedRate(this::refreshItems, 0, fixedUpdateRateMs, TimeUnit.MILLISECONDS);
        }
    }

    public static void shutdownRefreshers() {
        scheduler.shutdown();
    }

    public void refreshItems() {
        //System.out.println("Loading " + this.getClass().getSimpleName());
        long start = System.currentTimeMillis();
        this.items = this.findAll();
        this.lastRefreshed = System.currentTimeMillis();
        this.refreshDuration = this.lastRefreshed - start;

        synchronized (this.initLock) {
            this.itemsInitialized = true;
            //System.out.println("Took " + (lastRefreshed - start) + "ms to load " + this.getClass().getSimpleName());
            this.initLock.notifyAll();
        }
    }

    private <S> S waitForInitLock(@NonNull Supplier<S> function) throws SqlException {
        try {
            synchronized (this.initLock) {
                while (!this.itemsInitialized)
                    this.initLock.wait();

                return function.get();
            }
        } catch (InterruptedException exception) {
            throw new SqlException("Could not wait for items to be initialized.", exception);
        }
    }

    public ConcurrentList<T> findAll() {
        return this.sqlSession.with((ReturnSessionFunction<ConcurrentList<T>>) this::findAll);
    }

    public ConcurrentList<T> findAll(@NonNull Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(this.tClass);
        Root<T> rootEntry = cq.from(this.tClass);
        CriteriaQuery<T> all = cq.select(rootEntry);

        return Concurrent.newList(session
                .createQuery(all)
                .setCacheable(true)
                .getResultList()
        );
    }

    public ConcurrentList<T> findAllCached() throws SqlException {
        return this.waitForInitLock(() -> Concurrent.newList(this.items));
    }

    @SuppressWarnings({"unchecked", "varargs"}) // Written safely
    public <S> ConcurrentList<T> findAllOrEmptyCached(Pair<FilterFunction<T, S>, S>... predicates) throws SqlException {
        ConcurrentList<T> itemsCopy = this.findAllCached();
        ConcurrentList<T> allItems = Concurrent.newList();

        if (ListUtil.notEmpty(itemsCopy)) {
            allItems.addAll(itemsCopy.stream()
                    .filter(it -> {
                        boolean match = false;

                        for (Pair<FilterFunction<T, S>, S> pair : predicates)
                            match |= Objects.equals(pair.getKey().apply(it), pair.getValue());

                        return match;
                    })
                    .collect(Concurrent.toList())
            );
        }

        return allItems;
    }

    public <S> T findFirstOrNull(@NonNull Session session, String field, S value) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(this.tClass);
        Root<T> rootEntry = query.from(this.tClass);
        CriteriaQuery<T> filtered = query.select(rootEntry).where(builder.equal(rootEntry.get(field), value));
        return session.createQuery(filtered).getSingleResult();
    }

    public <S> T findFirstOrNull(String field, S value) {
        return this.sqlSession.with(session -> {
            return findFirstOrNull(session, field, value);
        });
    }

    @SuppressWarnings({"unchecked", "varargs"}) // Written safely
    public <S> T findFirstOrNull(@NonNull Session session, Pair<String, S>... predicates) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(this.tClass);
        Root<T> rootEntry = cq.from(this.tClass);
        CriteriaQuery<T> filtered = cq.select(rootEntry);

        for (Pair<String, S> predicate : predicates)
            filtered = filtered.where(cb.equal(rootEntry.get(predicate.getKey()), predicate.getValue()));

        return session.createQuery(filtered).getSingleResult();
    }

    @SuppressWarnings({"unchecked", "varargs"}) // Written safely
    public <S> T findFirstOrNull(Pair<String, S>... predicates) {
        return this.sqlSession.with(session -> {
            return findFirstOrNull(session, predicates);
        });
    }

    public Object findFirstOrNullTest() throws SqlException {
        return null;
    }

    public <S> T findFirstOrNullCached(@NonNull FilterFunction<T, S> function, S value) throws SqlException {
        return this.waitForInitLock(
                () -> items.stream()
                        .filter(model -> Objects.equals(function.apply(model), value))
                        .findFirst()
                        .orElse(null)
        );
    }

    @SuppressWarnings({"unchecked", "varargs"}) // Written safely
    public <S> T findFirstOrNullCached(Pair<FilterFunction<T, S>, S>... predicates) throws SqlException {
        return this.findFirstOrNullCached(FilterFunction.Match.ALL, predicates);
    }

    @SuppressWarnings({"unchecked", "varargs"}) // Written safely
    public <S> T findFirstOrNullCached(FilterFunction.Match match, Pair<FilterFunction<T, S>, S>... predicates) throws SqlException {
        ConcurrentList<T> allMatches = this.findAllCached();
        ConcurrentList<T> anyMatches = Concurrent.newList();

        if (ListUtil.notEmpty(allMatches)) {
            for (Pair<FilterFunction<T, S>, S> pair : predicates) {
                ConcurrentList<T> tempList = allMatches.stream()
                        .filter(it -> Objects.equals(pair.getKey().apply(it), pair.getValue()))
                        .collect(Concurrent.toList());

                if (match == FilterFunction.Match.ALL)
                    allMatches = tempList; // This must only shorten allMatches if matching ALL, otherwise ANY will fail
                else if (match == FilterFunction.Match.ANY)
                    anyMatches.addAll(tempList);
            }
        }

        ConcurrentList<T> returnMatches = (match == FilterFunction.Match.ALL ? allMatches : anyMatches);
        return ListUtil.isEmpty(returnMatches) ? null : returnMatches.get(0);
    }

    @SuppressWarnings({"unchecked", "varargs"}) // Written safely
    public <S> T matchAnyFirstOrNullCached(Pair<FilterFunction<T, S>, S>... predicates) throws SqlException {
        ConcurrentList<T> itemsCopy = this.findAllCached();

        if (ListUtil.notEmpty(itemsCopy)) {
            for (Pair<FilterFunction<T, S>, S> pair : predicates) {
                itemsCopy = itemsCopy.stream()
                        .filter(it -> Objects.equals(pair.getKey().apply(it), pair.getValue()))
                        .collect(Concurrent.toList());
            }
        }

        return ListUtil.isEmpty(itemsCopy) ? null : itemsCopy.get(0);
    }

    public long save(Session session, T model) {
        return (long) session.save(model);
    }

    public long save(T model) {
        return this.sqlSession.transaction(session -> {
            return save(session, model);
        });
    }

    public void update(@NonNull Session session, T model) {
        session.update(model);
    }

    public void update(T model) {
        this.sqlSession.transaction(session -> {
            update(session, model);
        });
    }

    public void saveOrUpdate(@NonNull Session session, T model) {
        session.saveOrUpdate(model);
    }

    public void saveOrUpdate(T model) {
        this.sqlSession.transaction(session -> {
            saveOrUpdate(session, model);
        });
    }

}
