package dev.sbs.api.data.sql;

import dev.sbs.api.data.Repository;
import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.sql.exception.SqlException;
import dev.sbs.api.reflection.Reflection;
import dev.sbs.api.util.concurrent.Concurrent;
import dev.sbs.api.util.concurrent.ConcurrentList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.function.Function;

@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public abstract class SqlRepository<T extends SqlModel> extends Repository<T> {

    private final SqlSession sqlSession;
    private final Class<T> tClass;
    @Getter private final long startupTime;

    /**
     * Creates a new repository of type {@link T}.
     *
     * @param sqlSession the sql session to use
     */
    public SqlRepository(@NonNull SqlSession sqlSession) {
        this.sqlSession = sqlSession;
        this.tClass = Reflection.getSuperClass(this);
        long startTime = System.currentTimeMillis();
        this.findAll();
        this.startupTime = System.currentTimeMillis() - startTime;
    }

    @Override
    public final ConcurrentList<T> findAll() throws SqlException {
        return this.sqlSession.with((Function<Session, ? extends ConcurrentList<T>>) this::findAll);
    }

    public final ConcurrentList<T> findAll(@NonNull Session session) throws SqlException {
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(this.getTClass());
            Root<T> rootEntry = criteriaQuery.from(this.getTClass());
            CriteriaQuery<T> all = criteriaQuery.select(rootEntry);

            return Concurrent.newList(
                session.createQuery(all)
                    .setCacheRegion(this.getTClass().getName())
                    .setCacheable(true)
                    .getResultList()
            );
        } catch (Exception exception) {
            throw new SqlException(exception);
        }
    }

    public T save(T model) throws SqlException {
        return this.sqlSession.transaction(session -> {
            return this.save(session, model);
        });
    }

    public T save(Session session, T model) throws SqlException {
        try {
            Serializable identifier = session.save(model);
            return session.get(this.tClass, identifier);
        } catch (Exception exception) {
            throw new SqlException(exception);
        }
    }

    public T update(T model) throws SqlException {
        return this.sqlSession.transaction(session -> {
            return this.update(session, model);
        });
    }

    public T update(@NonNull Session session, T model) throws SqlException {
        try {
            session.update(model);
            return model;
        } catch (NonUniqueObjectException nuoException) {
            return model;
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
