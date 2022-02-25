package dev.sbs.api.data.sql;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.TestConfig;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.io.File;

public class SqlSessionTest {

    private static final TestConfig testConfig;

    static {
        try {
            File currentDir = new File(SimplifiedApi.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            testConfig = new TestConfig(currentDir.getParentFile(), "testsql");
        } catch (Exception exception) {
            throw new IllegalArgumentException("Unable to retrieve current directory", exception); // Should never get here
        }
    }

    @Test
    public void openSession_ok() {
        SimplifiedApi.connectDatabase(testConfig);
        Session session = SimplifiedApi.getSqlSession().openSession();
        MatcherAssert.assertThat(session, Matchers.notNullValue());
        session.close();
    }

}
