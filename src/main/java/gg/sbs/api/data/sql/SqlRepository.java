package gg.sbs.api.data.sql;

import gg.sbs.api.data.sql.exception.SqlException;
import gg.sbs.api.data.sql.model.SqlModel;
import gg.sbs.api.util.Pair;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static gg.sbs.api.util.TimeUtil.ONE_MINUTE_MS;

abstract public class SqlRepository<T extends SqlModel> {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(0);
    private static final Object schedulerLock = new Object();

    private final Class<T> tClass;
    private List<T> items = new ArrayList<>();
    private Boolean itemsInitialized = false;
    private final Object initLock = new Object();

    @SuppressWarnings("unchecked")
    public SqlRepository(long fixedRateMs) {
        ParameterizedType superClass = (ParameterizedType) this.getClass().getGenericSuperclass();
        tClass = (Class<T>) superClass.getActualTypeArguments()[0];
        synchronized (schedulerLock) {
            scheduler.scheduleAtFixedRate(this::refreshItems, 0, fixedRateMs, TimeUnit.MILLISECONDS);
        }
    }

    public SqlRepository() {
        this(ONE_MINUTE_MS);
    }

    public static void shutdownRefreshers() {
        scheduler.shutdown();
    }

    public void refreshItems() {
        items = findAll();
        synchronized (initLock) {
            itemsInitialized = true;
            initLock.notifyAll();
        }
    }

    private <S> S waitForInitLock(WaitFunction<S> f) throws SqlException {
        try {
            synchronized (initLock) {
                while (!itemsInitialized) initLock.wait();
                return f.returns();
            }
        } catch (InterruptedException e) {
            throw new SqlException("Could not wait for items to be initialized: " + e.getMessage());
        }
    }

    public interface WaitFunction<S> {
        S returns();
    }

    public List<T> findAllCached() throws SqlException {
        return waitForInitLock(() -> new ArrayList<>(items));
    }

    public <S> T findFirstOrNullCached(FilterFunction<T, S> f, S s) throws SqlException {
        return waitForInitLock(() -> items.stream().filter(it -> f.returns(it).equals(s)).findFirst().orElse(null));
    }

    @SuppressWarnings({"unchecked", "varargs"}) // Written safely
    public <S> T findFirstOrNullCached(Pair<FilterFunction<T, S>, S>... predicates) throws SqlException {
        List<T> itemsCopy = waitForInitLock(() -> new ArrayList<>(items));
        for (int i = 0; i < predicates.length && itemsCopy.size() > 0; i++) {
            Pair<FilterFunction<T, S>, S> q = predicates[i];
            itemsCopy = itemsCopy.stream()
                    .filter(it -> Objects.equals(q.getFirst().returns(it), q.getSecond()))
                    .collect(Collectors.toList());
        }
        if (itemsCopy.size() == 0) return null;
        else return itemsCopy.get(0);
    }

    public interface FilterFunction<T extends SqlModel, S> {
        S returns(T t);
    }

    public List<T> findAll(Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(tClass);
        Root<T> rootEntry = cq.from(tClass);
        CriteriaQuery<T> all = cq.select(rootEntry);
        return session.createQuery(all).getResultList();
    }

    public List<T> findAll() {
        return SqlSessionUtil.withSession((SqlSessionUtil.ReturnSessionFunction<List<T>>) this::findAll);
    }

    public <S> T findFirstOrNull(Session session, String field, S value) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(tClass);
        Root<T> rootEntry = cq.from(tClass);
        CriteriaQuery<T> filtered = cq.select(rootEntry).where(cb.equal(rootEntry.get(field), value));
        return session.createQuery(filtered).getSingleResult();
    }

    public <S> T findFirstOrNull(String field, S value) {
        return SqlSessionUtil.withSession(session -> {
            return findFirstOrNull(session, field, value);
        });
    }

    @SuppressWarnings({"unchecked", "varargs"}) // Written safely
    public <S> T findFirstOrNull(Session session, Pair<String, S>... predicates) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(tClass);
        Root<T> rootEntry = cq.from(tClass);
        CriteriaQuery<T> filtered = cq.select(rootEntry);
        for (Pair<String, S> predicate : predicates) {
            filtered = filtered.where(cb.equal(rootEntry.get(predicate.getFirst()), predicate.getSecond()));
        }
        return session.createQuery(filtered).getSingleResult();
    }

    @SuppressWarnings({"unchecked", "varargs"}) // Written safely
    public <S> T findFirstOrNull(Pair<String, S>... predicates) {
        return SqlSessionUtil.withSession(session -> {
            return findFirstOrNull(session, predicates);
        });
    }

    public long save(Session session, T t) {
        return (long) (Long) session.save(t);
    }

    public long save(T t) {
        return SqlSessionUtil.withTransaction(session -> {
            return save(session, t);
        });
    }

    public void update(Session session, T t) {
        session.update(t);
    }

    public void update(T t) {
        SqlSessionUtil.withTransaction(session -> {
            update(session, t);
        });
    }

    public void saveOrUpdate(Session session, T t) {
        session.saveOrUpdate(t);
    }

    public void saveOrUpdate(T t) {
        SqlSessionUtil.withTransaction(session -> {
            saveOrUpdate(session, t);
        });
    }

}