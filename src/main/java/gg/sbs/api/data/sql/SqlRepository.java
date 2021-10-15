package gg.sbs.api.data.sql;

import gg.sbs.api.data.sql.exception.SqlException;
import gg.sbs.api.data.sql.function.FilterFunction;
import gg.sbs.api.data.sql.function.ReturnSessionFunction;
import gg.sbs.api.data.sql.model.SqlModel;
import gg.sbs.api.util.FormatUtil;
import gg.sbs.api.util.ListUtil;
import gg.sbs.api.util.Pair;
import gg.sbs.api.util.concurrent.Concurrent;
import gg.sbs.api.util.concurrent.ConcurrentList;
import gg.sbs.api.util.function.ReturnFunction;
import lombok.AccessLevel;
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

import static gg.sbs.api.util.TimeUtil.ONE_MINUTE_MS;

@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public abstract class SqlRepository<T extends SqlModel> {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(0);
    private static final Object schedulerLock = new Object();

    private final Object initLock = new Object();
    private final ConcurrentList<T> items = Concurrent.newList();
    private final Class<T> tClass;
    private final SqlSession sqlSession;
    private Boolean itemsInitialized = false;

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
        this.items.clear();
        this.items.addAll(this.findAll());

        synchronized (this.initLock) {
            this.itemsInitialized = true;
            this.initLock.notifyAll();
        }
    }

    private <S> S waitForInitLock(@NonNull ReturnFunction<S> function) throws SqlException {
        try {
            synchronized (this.initLock) {
                while (!this.itemsInitialized)
                    this.initLock.wait();

                return function.handle();
            }
        } catch (InterruptedException exception) {
            throw new SqlException(FormatUtil.format("Could not wait for items to be initialized: {0}", exception.getMessage()), exception);
        }
    }

    public ConcurrentList<T> findAllCached() throws SqlException {
        return waitForInitLock(() -> Concurrent.newList(this.items));
    }

    public <S> T findFirstOrNullCached(@NonNull FilterFunction<T, S> function, S value) throws SqlException {
        return waitForInitLock(
                () -> items.stream()
                        .filter(it -> Objects.equals(function.handle(it), value))
                        .findFirst()
                        .orElse(null)
        );
    }

    @SuppressWarnings({"unchecked", "varargs"}) // Written safely
    public <S> T findFirstOrNullCached(Pair<FilterFunction<T, S>, S>... predicates) throws SqlException {
        ConcurrentList<T> itemsCopy = waitForInitLock(() -> Concurrent.newList(this.items));

        for (int i = 0; i < predicates.length && itemsCopy.size() > 0; i++) {
            Pair<FilterFunction<T, S>, S> q = predicates[i];
            itemsCopy = itemsCopy.stream()
                    .filter(it -> Objects.equals(q.getFirst().handle(it), q.getSecond()))
                    .collect(Concurrent.toList());
        }

        if (ListUtil.isEmpty(itemsCopy))
            return null;

        else return itemsCopy.get(0);
    }

    public ConcurrentList<T> findAll(@NonNull Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(this.tClass);
        Root<T> rootEntry = cq.from(this.tClass);
        CriteriaQuery<T> all = cq.select(rootEntry);
        return Concurrent.newList(session.createQuery(all).getResultList());
    }

    public ConcurrentList<T> findAll() {
        return this.sqlSession.with((ReturnSessionFunction<ConcurrentList<T>>) this::findAll);
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
            filtered = filtered.where(cb.equal(rootEntry.get(predicate.getFirst()), predicate.getSecond()));

        return session.createQuery(filtered).getSingleResult();
    }

    @SuppressWarnings({"unchecked", "varargs"}) // Written safely
    public <S> T findFirstOrNull(Pair<String, S>... predicates) {
        return this.sqlSession.with(session -> {
            return findFirstOrNull(session, predicates);
        });
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