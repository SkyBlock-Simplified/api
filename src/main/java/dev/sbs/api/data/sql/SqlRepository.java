package dev.sbs.api.data.sql;

import dev.sbs.api.data.Repository;
import dev.sbs.api.data.exception.DataException;
import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.sql.exception.SqlException;
import dev.sbs.api.util.SimplifiedException;
import lombok.Getter;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.function.Function;
import java.util.stream.Stream;

public class SqlRepository<T extends SqlModel> extends Repository<T> {

    private final @NotNull SqlSession sqlSession;
    @Getter private final long startupTime;

    /**
     * Creates a new repository of type {@link T}.
     *
     * @param sqlSession the sql session to use
     */
    public SqlRepository(@NotNull SqlSession sqlSession, @NotNull Class<T> type) {
        super(type);
        this.sqlSession = sqlSession;
        long startTime = System.currentTimeMillis();
        this.findAll();
        this.startupTime = System.currentTimeMillis() - startTime;
    }

    @Override
    public final @NotNull Stream<T> stream() throws DataException {
        return this.sqlSession.with((Function<Session, Stream<T>>) this::stream);
    }

    public final @NotNull Stream<T> stream(@NotNull Session session) throws DataException {
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(this.getType());
            Root<T> rootEntry = criteriaQuery.from(this.getType());
            CriteriaQuery<T> all = criteriaQuery.select(rootEntry);

            return session.createQuery(all)
                .setCacheRegion(this.getType().getName())
                .setCacheable(true)
                .getResultList() // Dirty
                .stream();
        } catch (Exception exception) {
            throw SimplifiedException.of(SqlException.class)
                .withCause(exception)
                .build();
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
            return session.get(this.getType(), identifier);
        } catch (Exception exception) {
            throw SimplifiedException.of(SqlException.class)
                .withCause(exception)
                .build();
        }
    }

    public T update(T model) throws SqlException {
        return this.sqlSession.transaction(session -> {
            return this.update(session, model);
        });
    }

    public T update(@NotNull Session session, T model) throws SqlException {
        try {
            session.update(model);
            return model;
        } catch (NonUniqueObjectException nuoException) {
            return model;
        } catch (Exception exception) {
            throw SimplifiedException.of(SqlException.class)
                .withCause(exception)
                .build();
        }
    }

    public void saveOrUpdate(T model) throws SqlException {
        this.sqlSession.transaction(session -> {
            this.saveOrUpdate(session, model);
        });
    }

    public void saveOrUpdate(@NotNull Session session, T model) throws SqlException {
        try {
            session.saveOrUpdate(model);
        } catch (Exception exception) {
            throw SimplifiedException.of(SqlException.class)
                .withCause(exception)
                .build();
        }
    }

}
