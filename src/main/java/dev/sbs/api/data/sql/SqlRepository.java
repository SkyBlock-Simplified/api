package dev.sbs.api.data.sql;

import dev.sbs.api.data.Repository;
import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.sql.exception.SqlException;
import dev.sbs.api.data.sql.function.ReturnSessionFunction;
import dev.sbs.api.util.concurrent.Concurrent;
import dev.sbs.api.util.concurrent.ConcurrentList;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public abstract class SqlRepository<T extends SqlModel> extends Repository<T> {

    private final SqlSession sqlSession;

    /**
     * Creates a new repository of type {@link T}.
     *
     * @param sqlSession the sql session to use
     */
    public SqlRepository(@NonNull SqlSession sqlSession) {
        this.sqlSession = sqlSession;
        this.findAll();
    }

    @Override
    public final ConcurrentList<T> findAll() throws SqlException {
        return this.sqlSession.with((ReturnSessionFunction<ConcurrentList<T>>) this::findAll);
    }

    public final ConcurrentList<T> findAll(@NonNull Session session) throws SqlException {
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(this.getTClass());
            Root<T> rootEntry = cq.from(this.getTClass());
            CriteriaQuery<T> all = cq.select(rootEntry);

            return Concurrent.newList(
                session
                    .createQuery(all)
                    .setCacheable(true)
                    .getResultList()
            );
        } catch (Exception exception) {
            throw new SqlException(exception);
        }
    }

    public long save(T model) throws SqlException {
        return this.sqlSession.transaction(session -> {
            return this.save(session, model);
        });
    }

    public long save(Session session, T model) throws SqlException {
        try {
            return (long) session.save(model);
        } catch (Exception exception) {
            throw new SqlException(exception);
        }
    }

    public void update(T model) throws SqlException {
        this.sqlSession.transaction(session -> {
            this.update(session, model);
        });
    }

    public void update(@NonNull Session session, T model) throws SqlException {
        try {
            session.update(model);
        } catch (Exception exception) {
            throw new SqlException(exception);
        }
    }

    public void saveOrUpdate(T model) throws SqlException {
        this.sqlSession.transaction(session -> {
            this.saveOrUpdate(session, model);
        });
    }

    public void saveOrUpdate(@NonNull Session session, T model) throws SqlException {
        try {
            session.saveOrUpdate(model);
        } catch (Exception exception) {
            throw new SqlException(exception);
        }
    }

}
