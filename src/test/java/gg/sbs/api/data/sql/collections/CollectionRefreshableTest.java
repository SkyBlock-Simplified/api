package gg.sbs.api.data.sql.collections;

import gg.sbs.api.SimplifiedApi;
import gg.sbs.api.data.sql.exception.SqlException;
import gg.sbs.api.data.sql.models.collections.CollectionModel;
import gg.sbs.api.data.sql.models.collections.CollectionRefreshable;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CollectionRefreshableTest {
    private static final CollectionRefreshable collectionRefreshable;

    static {
        SimplifiedApi.enableDatabase();
        collectionRefreshable = SimplifiedApi.getSqlRefreshable(CollectionRefreshable.class);
    }

    @Test
    public void getCachedList_ok() throws SqlException {
        List<CollectionModel> cachedList = collectionRefreshable.getCachedList();
        MatcherAssert.assertThat(cachedList.size(), Matchers.greaterThan(0));
    }
}
