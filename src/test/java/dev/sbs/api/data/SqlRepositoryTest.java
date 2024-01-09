package dev.sbs.api.data;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.items.ItemModel;
import dev.sbs.api.data.model.skyblock.stats.StatModel;
import dev.sbs.api.data.model.skyblock.stats.StatSqlModel;
import dev.sbs.api.data.sql.SqlConfig;
import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.scheduler.Scheduler;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.search.SearchFunction;
import dev.sbs.api.util.helper.DataUtil;
import dev.sbs.api.util.mutable.pair.Pair;
import dev.sbs.api.util.mutable.primitive.MutableInt;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import javax.persistence.Table;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SqlRepositoryTest {

    @Test
    public void checkRepositories_ok() {
        try {
            System.out.println("Database Starting... ");
            SimplifiedApi.getSessionManager().connect(SqlConfig.defaultSql());
            System.out.println("Database initialized in " + SimplifiedApi.getSessionManager().getSession().getInitialization() + "ms");
            System.out.println("Database started in " + SimplifiedApi.getSessionManager().getSession().getStartup() + "ms");

            //System.out.println("QUERYING RARITIES #1");
            // Retrieve object from the database
            //Session session = SimplifiedApi.getSqlSession().getSessionFactory().openSession();
            //Transaction transaction = session.beginTransaction();

            /*ItemSqlModel rarity1 = session.get(ItemSqlModel.class, "WHEAT_GENERATOR_10");
            System.out.println(rarity1);
            transaction.commit();
            session.close();

            System.out.println("QUERYING RARITIES #2");
            // Retrieve object from the cache
            session = SimplifiedApi.getSqlSession().getSessionFactory().openSession();
            transaction = session.beginTransaction();
            ItemSqlModel person2 = session.get(ItemSqlModel.class, "WHEAT_GENERATOR_10");
            System.out.println(person2);
            transaction.commit();
            session.close();*/

            final SqlRepository<StatSqlModel> statRepository = (SqlRepository<StatSqlModel>) SimplifiedApi.getRepositoryOf(StatSqlModel.class);
            long time_loopStart = System.currentTimeMillis();
            long time_loopEnd = time_loopStart + (3 * 60 * 1000);
            final MutableInt repeat = new MutableInt();

            Scheduler scheduler = new Scheduler();
            scheduler.scheduleAsync(() -> {
                StatSqlModel statSqlModel = statRepository.findFirstOrNull(StatModel::getKey, "MAGIC_FIND");
                statSqlModel.setName("Magic Find");
                statSqlModel.update();
            }, 0L, 35L, TimeUnit.SECONDS);

            while (System.currentTimeMillis() < time_loopEnd) {
                long beforeFind = System.currentTimeMillis();
                StatSqlModel statSqlModel = statRepository.findFirstOrNull(StatModel::getKey, "MAGIC_FIND");
                long afterFind = System.currentTimeMillis();
                long diff = afterFind - beforeFind;
                System.out.printf("Getting %s:%s (%s) took: %s%n", statSqlModel.getKey(), statSqlModel.getName(), repeat.getAndIncrement(), diff);

                Scheduler.sleep(3 * 1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        SimplifiedApi.getSessionManager().disconnect();
    }

    @Test
    public void searchTest_ok() {
        System.out.println("Database Starting... ");
        SimplifiedApi.getSessionManager().connect(SqlConfig.defaultSql());
        System.out.println("Database initialized in " + SimplifiedApi.getSessionManager().getSession().getInitialization() + "ms");
        System.out.println("Database started in " + SimplifiedApi.getSessionManager().getSession().getStartup() + "ms");

        runTest(StatModel.class, StatModel::getKey, "MAGIC_FIND");
        runTest(ItemModel.class, ItemModel::getItemId, "WITHER_RELIC");
    }

    private static <T extends Model, S> void runTest(@NotNull Class<T> model, @NotNull SearchFunction<T, S> function, @NotNull S value) {
        long before1 = ctm();
        ConcurrentList<T> result1 = SimplifiedApi.getRepositoryOf(model).findAll();
        System.out.printf("%s: 1.1 took %s%n", model.getSimpleName(), (ctm() - before1));

        long before2 = ctm();
        T result2 = result1.findFirstOrNull(function, value);
        System.out.printf("%s: 2.1 took %s%n", model.getSimpleName(), (ctm() - before2));

        long before4 = ctm();
        T model4 = SimplifiedApi.getRepositoryOf(model).findFirstOrNull(function, value);
        System.out.printf("%s: 4.1 took %s%n", model.getSimpleName(), (ctm() - before4));

        long before42 = ctm();
        T model42 = SimplifiedApi.getRepositoryOf(model).findAll().findFirstOrNull(function, value);
        System.out.printf("%s: 4.2 took %s%n", model.getSimpleName(), (ctm() - before42));
    }

    public static long ctm() {
        return System.currentTimeMillis();
    }

    @Test
    @SuppressWarnings("all")
    public void dumpDatabaseToJson_ok() {
        File currentDir = SimplifiedApi.getCurrentDirectory();
        File dbDir = new File(currentDir, "build/db");

        if (!dbDir.exists())
            dbDir.mkdirs();

        System.out.println("Connecting to database...");
        SimplifiedApi.getSessionManager().connect(SqlConfig.defaultSql());

        SimplifiedApi.getSessionManager()
            .getSession()
            .getModels()
            .stream()
            .map(modelClass -> (Class<? extends Model>) modelClass)
            .filter(modelClass -> modelClass.isAnnotationPresent(Table.class))
            .map(modelClass -> Pair.of(
                modelClass,
                modelClass.getAnnotation(Table.class).name()
            ))
            .forEach(entry -> {
                String tableName = entry.getValue();
                System.out.println("Saving " + tableName + "...");

                try (FileWriter fileWriter = DataUtil.newFileWriter(dbDir.getAbsolutePath() + "/" + tableName + ".json")) {
                    fileWriter.write(SimplifiedApi.getGson().toJson(SimplifiedApi.getRepositoryOf(entry.getKey()).findAll()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
    }

}
