package gg.sbs.api.data.sql;

import gg.sbs.api.util.Pair;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

abstract public class SqlRepository<T extends SqlModel> {

    protected List<T> findAllImpl(Class<T> tClass) {
        Session session = SqlSessionUtil.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(tClass);
        Root<T> rootEntry = cq.from(tClass);
        CriteriaQuery<T> all = cq.select(rootEntry);
        return session.createQuery(all).getResultList();
    }

    protected <S> T findFirstOrNullImpl(Class<T> tClass, String field, S value) {
        Session session = SqlSessionUtil.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(tClass);
        Root<T> rootEntry = cq.from(tClass);
        CriteriaQuery<T> filtered = cq.select(rootEntry).where(cb.equal(rootEntry.get(field), value));
        return session.createQuery(filtered).getSingleResult();
    }

    @SuppressWarnings({"unchecked", "varargs"}) // Written safely
    protected <S> T findFirstOrNullImpl(Class<T> tClass, Pair<String, S>... predicates) {
        Session session = SqlSessionUtil.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(tClass);
        Root<T> rootEntry = cq.from(tClass);
        CriteriaQuery<T> filtered = cq.select(rootEntry);
        for (Pair<String, S> predicate : predicates) {
            filtered = filtered.where(cb.equal(rootEntry.get(predicate.getFirst()), predicate.getSecond()));
        }
        return session.createQuery(filtered).getSingleResult();
    }

    public long save(T t) {
        Session session = SqlSessionUtil.openSession();
        return (Long) session.save(t);
    }

    public void update(T t) {
        Session session = SqlSessionUtil.openSession();
        session.update(t);
    }

    public void saveOrUpdate(T t) {
        Session session = SqlSessionUtil.openSession();
        session.saveOrUpdate(t);
    }

    abstract public List<T> findAll();

    abstract public <S> T findFirstOrNull(String field, S value);

    @SuppressWarnings({"unchecked", "varargs"}) // Written safely
    abstract public <S> T findFirstOrNull(Pair<String, S>... predicates);
}