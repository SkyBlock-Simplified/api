package dev.sbs.api.data;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.sql.SqlConfig;
import dev.sbs.api.data.sql.SqlSession;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

public class SqlSessionTest {

    @Test
    public void openSession_ok() {
        SimplifiedApi.getSessionManager().connect(SqlConfig.defaultSql());
        Session session = SimplifiedApi.getSessionManager().getSession().asType(SqlSession.class).openSession();
        MatcherAssert.assertThat(session, Matchers.notNullValue());
        session.close();
    }

}
