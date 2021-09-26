package gg.sbs.api.data.sql;

import gg.sbs.api.SimplifiedAPI;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class SqlRefreshable<T extends SqlModel, R extends SqlRepository<T>> {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(0);
    private final List<T> items = new ArrayList<>();
    private boolean itemsInitialized = false;

    public SqlRefreshable(Class<R> rClass, long fixedRateMs) {
        scheduler.scheduleAtFixedRate(() -> {
            Collections.copy(items, SimplifiedAPI.getSqlRepository(rClass).findAll());
            itemsInitialized = true;
        }, 0, fixedRateMs, TimeUnit.MILLISECONDS);
    }

    public List<T> getCachedList() throws SQLException {
        if (!itemsInitialized) throw new SQLException("Items have not been initialized yet");
        List<T> itemsCopy = new ArrayList<>();
        Collections.copy(itemsCopy, items);
        return itemsCopy;
    }

    public <S> T findFirst(FilterFunction<T, S> f, S s) throws SQLException {
        if (!itemsInitialized) throw new SQLException("Items have not been initialized yet");
        return items.stream().filter(T -> f.returns(T) == s).findFirst().orElse(null);
    }

    public interface FilterFunction<T extends SqlModel, S> {
        S returns(T t);
    }
}
