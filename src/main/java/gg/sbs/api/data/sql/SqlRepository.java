package gg.sbs.api.data.sql;

import gg.sbs.api.data.sql.model.SqlModel;
import gg.sbs.api.util.Pair;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.util.List;

abstract public class SqlRepository<T extends SqlModel> {

    private final Class<T> tClass;

    @SuppressWarnings("unchecked")
    public SqlRepository(){
        ParameterizedType superClass = (ParameterizedType) this.getClass().getGenericSuperclass();
        tClass = (Class<T>) superClass.getActualTypeArguments()[0];
    }

    public List<T> findAll(Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(tClass);
        Root<T> rootEntry = cq.from(tClass);
        CriteriaQuery<T> all = cq.select(rootEntry);
        return session.createQuery(all).getResultList();
    }

    public List<T> findAll() {
        Session session = SqlSessionUtil.openSession();
        List<T> result = findAll(session);
        session.close();
        return result;
    }

    public <S> T findFirstOrNull(Session session, String field, S value) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(tClass);
        Root<T> rootEntry = cq.from(tClass);
        CriteriaQuery<T> filtered = cq.select(rootEntry).where(cb.equal(rootEntry.get(field), value));
        return session.createQuery(filtered).getSingleResult();
    }

    public <S> T findFirstOrNull(String field, S value) {
        Session session = SqlSessionUtil.openSession();
        T result = findFirstOrNull(session, field, value);
        session.close();
        return result;
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
        Session session = SqlSessionUtil.openSession();
        T result = findFirstOrNull(session, predicates);
        session.close();
        return result;
    }

    public long save(Session session, T t) {
        return (long) (Long) session.save(t);
    }

    public long save(T t) {
        Session session = SqlSessionUtil.openSession();
        Transaction tx = session.beginTransaction();
        long result = save(session, t);
        session.flush();
        tx.commit();
        session.close();
        return result;
    }

    public void update(Session session, T t) {
        session.update(t);
    }

    public void update(T t) {
        Session session = SqlSessionUtil.openSession();
        Transaction tx = session.beginTransaction();
        update(session, t);
        session.flush();
        tx.commit();
        session.close();
    }

    public void saveOrUpdate(Session session, T t) {
        session.saveOrUpdate(t);
    }

    public void saveOrUpdate(T t) {
        Session session = SqlSessionUtil.openSession();
        Transaction tx = session.beginTransaction();
        saveOrUpdate(session, t);
        session.flush();
        tx.commit();
        session.close();
    }

}