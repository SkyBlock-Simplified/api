package gg.sbs.api.data.sql;

import gg.sbs.api.SimplifiedApi;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

public class SqlSessionUtilTest {

    @Test
    public void openSession_ok() {
        SimplifiedApi.enableDatabase();
        Session session = SimplifiedApi.getSqlSession().openSession();
        MatcherAssert.assertThat(session, Matchers.notNullValue());
        session.close();
    }

}