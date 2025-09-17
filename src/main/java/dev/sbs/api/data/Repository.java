package dev.sbs.api.data;

import dev.sbs.api.collection.concurrent.Concurrent;
import dev.sbs.api.collection.concurrent.ConcurrentList;
import dev.sbs.api.collection.search.Sortable;
import dev.sbs.api.data.exception.DataException;
import org.hibernate.Hibernate;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.time.Duration;
import java.util.stream.Stream;

public interface Repository<T extends Model> extends Sortable<T> {

    @NotNull Duration getCacheDuration();

    @NotNull CacheExpiry getCacheExpiry();

    long getLastUpdated();

    long getLastUpdateDuration();

    @NotNull DataSession<T> getSession();

    long getStartup();

    /**
     * Retrieves the class type of the {@link Model} associated with this repository.
     *
     * @return The {@link Model} representing the type of the model.
     */
    @NotNull Class<T> getType();

    void refresh() throws DataException;

    /**
     * Returns the cached {@link ResultSet} from {@link Hibernate} collected in {@link Stream<T>}.
     */
    @Override
    @NotNull Stream<T> stream() throws DataException;

    /**
     * Returns an updated {@link ResultSet} from {@link Hibernate} collected in {@link ConcurrentList}.
     */
    default @NotNull ConcurrentList<T> findAll() throws DataException {
        return this.stream().collect(Concurrent.toUnmodifiableList());
    }

    default boolean isStale() {
        return System.currentTimeMillis() - this.getLastUpdated() >= this.getCacheDuration().toMillis();
    }

}
