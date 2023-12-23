package dev.sbs.api.data;

import dev.sbs.api.data.exception.DataException;
import dev.sbs.api.data.model.Model;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.search.Sortable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.util.stream.Stream;

@RequiredArgsConstructor
public abstract class Repository<T extends Model> implements Sortable<T> {

    @Getter protected final @NotNull Class<T> type;
    protected @NotNull ConcurrentList<T> cache = Concurrent.newList();

    /**
     * Returns an updated {@link ResultSet} from {@link Hibernate} collected in {@link Stream<T>}.
     */
    @Override
    public final @NotNull Stream<T> stream() throws DataException {
        return this.findAll().stream();
    }

    /**
     * Returns an updated {@link ResultSet} from {@link Hibernate} collected in {@link ConcurrentList}.
     */
    public abstract @NotNull ConcurrentList<T> findAll();

    /**
     * Returns the cached {@link ResultSet} from {@link Hibernate} collected in {@link ConcurrentList<T>}.
     */
    public final @NotNull ConcurrentList<T> findCached() {
        return this.cache;
    }

}
