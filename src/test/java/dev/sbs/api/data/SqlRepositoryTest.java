package dev.sbs.api.data;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.TestConfig;
import dev.sbs.api.data.model.skyblock.stats.StatModel;
import dev.sbs.api.data.model.skyblock.stats.StatSqlModel;
import dev.sbs.api.data.sql.SqlRepository;
import org.junit.jupiter.api.Test;

import java.io.File;

public class SqlRepositoryTest {

    private static final TestConfig testConfig;

    static {
        try {
            File currentDir = new File(SimplifiedApi.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            testConfig = new TestConfig();
        } catch (Exception exception) {
            throw new IllegalArgumentException("Unable to retrieve current directory", exception); // Should never get here
        }
    }

    @Test
    public void checkRepositories_ok() {
        try {
            System.out.println("Connecting to database...");
            SimplifiedApi.getSessionManager().connectSql(testConfig);
            //testConfig.setLoggingLevel(Level.DEBUG);



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

            SqlRepository<StatSqlModel> statRepository = (SqlRepository<StatSqlModel>) SimplifiedApi.getRepositoryOf(StatSqlModel.class);
            StatSqlModel read = statRepository.findFirstOrNull(StatModel::getKey, "MAGIC_FIND");
            String name1 = read.getName();

        } catch (Exception e) {
            e.printStackTrace();
        }

        SimplifiedApi.getSessionManager().disconnect();
    }

}
