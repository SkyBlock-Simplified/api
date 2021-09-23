package gg.sbs.api.database;

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

    @SuppressWarnings("unused") // Will use later
    protected T findByIdImpl(Class<T> tClass, long i) {
        Session session = SqlSessionUtil.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(tClass);
        Root<T> rootEntry = cq.from(tClass);
        CriteriaQuery<T> filtered = cq.select(rootEntry).where(cb.equal(rootEntry.get("id"), i));
        return session.createQuery(filtered).getSingleResult();
    }

    abstract public List<T> findAll();
}
