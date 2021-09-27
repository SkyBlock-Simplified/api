package gg.sbs.api.data.sql;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

public class SqlSessionUtilTest {
    @Test
    public void openSession_ok() {
        Session session = SqlSessionUtil.openSession();
        MatcherAssert.assertThat(session, Matchers.notNullValue());
    }
}
