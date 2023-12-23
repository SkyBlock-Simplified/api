package dev.sbs.api.data.sql;

import dev.sbs.api.data.Repository;
import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.sql.exception.SqlException;
import dev.sbs.api.util.SimplifiedException;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import lombok.Getter;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.function.Function;

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
    public @NotNull ConcurrentList<T> findAll() {
        return this.sqlSession.with((Function<Session, ? extends ConcurrentList<T>>) this::findAll);
    }

    public @NotNull ConcurrentList<T> findAll(@NotNull Session session) {
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(this.getType());
            Root<T> rootEntry = criteriaQuery.from(this.getType());
            criteriaQuery = criteriaQuery.select(rootEntry);

            return this.cache = Concurrent.newUnmodifiableList(
                session.createQuery(criteriaQuery)
                    .setCacheRegion(this.getType().getName())
                    .setCacheable(true)
                    .getResultList() // Fastest
            );
        } catch (Exception exception) {
            throw SimplifiedException.of(SqlException.class)
                .withCause(exception)
                .build();
        }
    }

    public @NotNull T delete(@NotNull T model) throws SqlException {
        return this.sqlSession.transaction(session -> {
            return this.delete(session, model);
        });
    }

    public @NotNull T delete(@NotNull Session session, @NotNull T model) throws SqlException {
        try {
            session.delete(model);
            return model;
        } catch (Exception exception) {
            throw SimplifiedException.of(SqlException.class)
                .withCause(exception)
                .build();
        }
    }

    public @NotNull T save(@NotNull T model) throws SqlException {
        return this.sqlSession.transaction(session -> {
            return this.save(session, model);
        });
    }

    public @NotNull T save(@NotNull Session session, @NotNull T model) throws SqlException {
        try {
            Serializable serializable = session.save(model);
            return session.get(this.getType(), serializable);
        } catch (Exception exception) {
            throw SimplifiedException.of(SqlException.class)
                .withCause(exception)
                .build();
        }
    }

    public @NotNull T update(@NotNull T model) throws SqlException {
        return this.sqlSession.transaction(session -> {
            return this.update(session, model);
        });
    }

    public @NotNull T update(@NotNull Session session, @NotNull T model) throws SqlException {
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

}
