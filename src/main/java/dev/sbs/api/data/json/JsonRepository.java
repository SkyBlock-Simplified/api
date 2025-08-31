package dev.sbs.api.data.json;

import dev.sbs.api.data.Repository;
import dev.sbs.api.data.json.exception.JsonException;
import dev.sbs.api.data.sql.CacheExpiry;
import dev.sbs.api.data.sql.exception.SqlException;
import dev.sbs.api.scheduler.ScheduledTask;
import lombok.AccessLevel;
import lombok.Getter;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Stream;

@Getter
public class JsonRepository<T extends JsonModel> implements Repository<T> {

    private final @NotNull Class<T> type;
    private final @NotNull JsonSession session;
    @Getter(AccessLevel.PROTECTED)
    private final @NotNull ScheduledTask task;

    // Time
    private final @NotNull CacheExpiry cacheExpiry;
    private final @NotNull Duration cacheDuration;
    private final long startup;
    private long lastUpdated;
    private long lastUpdateDuration;

    /**
     * Creates a new repository of type {@link T}.
     *
     * @param session the json session to use
     */
    public JsonRepository(@NotNull JsonSession session, @NotNull Class<T> type) {
        this.type = type;
        this.session = session;
        this.cacheExpiry = Optional.ofNullable(type.getAnnotation(CacheExpiry.class)).orElse(CacheExpiry.DEFAULT);
        this.cacheDuration = Duration.of(cacheExpiry.value(), cacheExpiry.length().toChronoUnit());
        this.refresh();
        this.startup = this.lastUpdated;

        this.task = this.getSession().getScheduler().scheduleAsync(
            this::refresh,
            this.getCacheDuration().toMillis(),
            this.getCacheDuration().toMillis(),
            TimeUnit.MILLISECONDS
        );
    }

    @Override
    public @NotNull Stream<T> stream() throws JsonException {
        return this.getSession().with((Function<Session, ? extends Stream<T>>) this::stream);
    }

    public @NotNull Stream<T> stream(@NotNull Session session) throws JsonException {
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(this.getType());
            Root<T> rootEntry = criteriaQuery.from(this.getType());
            criteriaQuery = criteriaQuery.select(rootEntry);

            return session.createQuery(criteriaQuery)
                .setCacheRegion(this.getType().getName())
                .setCacheable(true)
                .getResultList() // Fastest
                .stream();
        } catch (Exception exception) {
            throw new JsonException(exception);
        }
    }

    public @NotNull T delete(@NotNull T model) throws JsonException {
        return this.getSession().transaction(session -> {
            return this.delete(session, model);
        });
    }

    public @NotNull T delete(@NotNull Session session, @NotNull T model) throws JsonException {
        try {
            session.delete(model);
            return model;
        } catch (Exception exception) {
            throw new SqlException(exception);
        }
    }

    public void refresh() throws JsonException {
        long refresh = System.currentTimeMillis();
        SessionFactory sessionFactory = this.getSession().getSessionFactory();

        // Evict From Cache
        if (sessionFactory.getCache() != null)
            sessionFactory.getCache().evict(this.getType());

        this.stream().close();
        this.lastUpdated = System.currentTimeMillis();
        this.lastUpdateDuration = this.getLastUpdated() - refresh;
    }

    public @NotNull T save(@NotNull T model) throws JsonException {
        return this.getSession().transaction(session -> {
            return this.save(session, model);
        });
    }

    public @NotNull T save(@NotNull Session session, @NotNull T model) throws JsonException {
        try {
            Serializable serializable = session.save(model);
            return session.get(this.getType(), serializable);
        } catch (Exception exception) {
            throw new JsonException(exception);
        }
    }

    public @NotNull T update(@NotNull T model) throws JsonException {
        return this.getSession().transaction(session -> {
            return this.update(session, model);
        });
    }

    public @NotNull T update(@NotNull Session session, @NotNull T model) throws JsonException {
        try {
            session.update(model);
            return model;
        } catch (NonUniqueObjectException nuoException) {
            return model;
        } catch (Exception exception) {
            throw new JsonException(exception);
        }
    }

}
