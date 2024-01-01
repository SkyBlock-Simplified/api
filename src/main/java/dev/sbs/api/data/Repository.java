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

@Getter
@RequiredArgsConstructor
public abstract class Repository<T extends Model> implements Sortable<T> {

    protected final @NotNull Class<T> type;

    /**
     * Returns the cached {@link ResultSet} from {@link Hibernate} collected in {@link Stream<T>}.
     */
    @Override
    public abstract @NotNull Stream<T> stream() throws DataException;

    /**
     * Returns an updated {@link ResultSet} from {@link Hibernate} collected in {@link ConcurrentList}.
     */
    public final @NotNull ConcurrentList<T> findAll() throws DataException {
        return this.stream().collect(Concurrent.toUnmodifiableList());
    }

}
