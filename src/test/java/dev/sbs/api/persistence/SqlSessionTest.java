package dev.sbs.api.persistence;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.persistence.sql.SqlConfig;
import dev.sbs.api.persistence.sql.SqlModel;
import dev.sbs.api.persistence.sql.SqlSession;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class SqlSessionTest {

    @Test
    public void openSession_ok() {
        Session<SqlModel> dataSession = SimplifiedApi.getSessionManager().connect(SqlConfig.defaultSql());
        org.hibernate.Session session = dataSession.asType(SqlSession.class).openSession();
        MatcherAssert.assertThat(session, Matchers.notNullValue());
        session.close();
    }

}
