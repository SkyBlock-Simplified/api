package dev.sbs.api.data.sql;

import ch.qos.logback.classic.Level;
import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.model.stats.StatSqlModel;
import dev.sbs.api.data.model.stats.StatSqlRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;

public class SqlRepositoryTest {

    @Test
    public void checkRepositories_ok() {
        try {
            System.out.println("ENABLING DATABASE");
            SimplifiedApi.enableDatabase();
            SimplifiedApi.getConfig().setLoggingLevel(Level.DEBUG);

            System.out.println("QUERYING RARITIES #1");
            // Retrieve object from the database
            Session session = SimplifiedApi.getSqlSession().getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();

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

            StatSqlRepository statSqlRepository = SimplifiedApi.getSqlRepository(StatSqlRepository.class);
            System.out.println("FINDING MAGIC FIND #1");
            StatSqlModel mfm1 = statSqlRepository.findFirstOrNull(StatSqlModel::getKey, "MAGIC_FIND");
            System.out.println("FINDING MAGIC FIND #2");
            StatSqlModel mfm2 = statSqlRepository.findFirstOrNull(StatSqlModel::getKey, "MAGIC_FIND");
            System.out.println("FINDING PRISTINE #1");
            StatSqlModel pm1 = statSqlRepository.findFirstOrNull(StatSqlModel::getKey, "PRISTINE");
            System.out.println("FINDING PRISTINE #2");
            StatSqlModel pm2 = statSqlRepository.findFirstOrNull(StatSqlModel::getKey, "PRISTINE");


        } catch (Exception e) {
            e.printStackTrace();
        }

        SimplifiedApi.getSqlSession().shutdown();
    }

}
