package gg.sbs.api.data.sql;

import gg.sbs.api.SimplifiedApi;
import gg.sbs.api.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SqlRefreshable<T extends SqlModel, R extends SqlRepository<T>> {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(0);
    private static final Object schedulerLock = new Object();

    private final SqlRepository<T> tRepository;
    private List<T> items = new ArrayList<>();
    private Boolean itemsInitialized = false;
    private final Object initLock = new Object();

    public SqlRefreshable(Class<R> rClass, long fixedRateMs) {
        tRepository = SimplifiedApi.getSqlRepository(rClass);
        synchronized (schedulerLock) {
            scheduler.scheduleAtFixedRate(this::refreshItems, 0, fixedRateMs, TimeUnit.MILLISECONDS);
        }
    }

    public static void shutdown() {
        scheduler.shutdown();
    }

    private void refreshItems() {
        items = tRepository.findAll();
        synchronized (initLock) {
            itemsInitialized = true;
            initLock.notifyAll();
        }
    }

    private <S> S waitForInitLock(WaitFunction<S> f) throws SqlException {
        try {
            synchronized (initLock) {
                while (!itemsInitialized) initLock.wait();
                return f.returns();
            }
        } catch (InterruptedException e) {
            throw new SqlException("Could not wait for items to be initialized: " + e.getMessage());
        }
    }

    public interface WaitFunction<S> {
        S returns();
    }

    public List<T> getCachedList() throws SqlException {
        return waitForInitLock(() -> new ArrayList<>(items));
    }

    public <S> T findFirstOrNull(FilterFunction<T, S> f, S s) throws SqlException {
        return waitForInitLock(() -> items.stream().filter(it -> f.returns(it).equals(s)).findFirst().orElse(null));
    }

    @SafeVarargs
    public final <S> T findFirstOrNull(Pair<FilterFunction<T, S>, S>... p) throws SqlException {
        List<T> itemsCopy = waitForInitLock(() -> new ArrayList<>(items));
        for (int i = 0; i < p.length && itemsCopy.size() > 0; i++) {
            Pair<FilterFunction<T, S>, S> q = p[i];
            itemsCopy = itemsCopy.stream()
                    .filter(it -> q.getFirst()
                            .returns(it)
                            .equals(q.getSecond()))
                    .collect(Collectors.toList());
        }
        if (itemsCopy.size() == 0) return null;
        else return itemsCopy.get(0);
    }

    public interface FilterFunction<T extends SqlModel, S> {
        S returns(T t);
    }
}
