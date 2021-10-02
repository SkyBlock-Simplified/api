package gg.sbs.api.data.sql.itemtypes;

import gg.sbs.api.SimplifiedApi;
import gg.sbs.api.data.sql.SqlException;
import gg.sbs.api.data.sql.models.itemtypes.ItemTypeModel;
import gg.sbs.api.data.sql.models.itemtypes.ItemTypeRefreshable;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ItemTypeRefreshableTest {
    private static final ItemTypeRefreshable itemTypeRefreshable;

    static {
        SimplifiedApi.enableDatabase();
        itemTypeRefreshable = SimplifiedApi.getSqlRefreshable(ItemTypeRefreshable.class);
    }

    @Test
    public void getCachedList_ok() throws SqlException {
        List<ItemTypeModel> cachedList = itemTypeRefreshable.getCachedList();
        MatcherAssert.assertThat(cachedList.size(), Matchers.greaterThan(0));
    }
}
