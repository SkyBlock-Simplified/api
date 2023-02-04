package dev.sbs.api.data;

import dev.sbs.api.data.exception.DataException;
import dev.sbs.api.data.model.Model;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.search.SearchQuery;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public abstract class Repository<T extends Model> implements SearchQuery<T, ConcurrentList<T>> {

    @Getter private final @NotNull Class<T> type;

    @Override
    public final @NotNull ConcurrentList<T> findAll() throws DataException {
        return this.stream().collect(Concurrent.toList());
    }

    @Override
    public final @NotNull ConcurrentList<T> toList(@NotNull Stream<T> stream) throws DataException {
        return stream.collect(Concurrent.toList());
    }

}
