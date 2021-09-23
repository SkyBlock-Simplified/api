package gg.sbs.api.database.repositories;

import gg.sbs.api.SimplifiedAPI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static gg.sbs.api.util.Consts.ONE_MINUTE_MS;

public class SqlRefreshable<T extends SqlModel, R extends SqlRepository<T>> {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(0);
    private static final long fixedRateMs = ONE_MINUTE_MS;
    private final List<T> items = new ArrayList<>();
    private boolean itemsInitialized = false;

    public SqlRefreshable(Class<R> rClass) {
        scheduler.scheduleAtFixedRate(() -> {
            Collections.copy(items, SimplifiedAPI.getSqlRepository(rClass).findAll());
            itemsInitialized = true;
        }, 0, fixedRateMs, TimeUnit.MILLISECONDS);
    }

    public List<T> getCachedList() throws SqlException {
        if (!itemsInitialized) throw new SqlException("Items have not been initialized yet");
        List<T> itemsCopy = new ArrayList<>();
        Collections.copy(itemsCopy, items);
        return itemsCopy;
    }
}
