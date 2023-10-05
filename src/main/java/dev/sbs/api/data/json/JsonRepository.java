package dev.sbs.api.data.json;

import dev.sbs.api.data.Repository;
import dev.sbs.api.data.exception.DataException;
import dev.sbs.api.data.model.JsonModel;
import dev.sbs.api.data.sql.exception.SqlException;
import dev.sbs.api.util.SimplifiedException;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public class JsonRepository<T extends JsonModel> extends Repository<T> {

    @Getter private final long startupTime;
    private final ConcurrentMap<T, Long> cache = Concurrent.newMap();

    /**
     * Creates a new repository of type {@link T}.
     */
    public JsonRepository(@NotNull Class<T> type) {
        super(type);
        long startTime = System.currentTimeMillis();
        this.findAll();
        this.startupTime = System.currentTimeMillis() - startTime;
    }

    @Override
    public final @NotNull Stream<T> stream() throws DataException {
        try {
            // TODO: Load json files into this.cache
            return null;
        } catch (Exception exception) {
            throw SimplifiedException.of(SqlException.class)
                .withCause(exception)
                .build();
        }
    }

}
