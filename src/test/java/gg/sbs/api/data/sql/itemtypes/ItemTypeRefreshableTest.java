package gg.sbs.api.data.sql.itemtypes;

import gg.sbs.api.SimplifiedAPI;
import gg.sbs.api.data.sql.models.accessories.AccessoryRefreshable;
import gg.sbs.api.data.sql.models.itemtypes.ItemTypeModel;
import gg.sbs.api.data.sql.models.itemtypes.ItemTypeRefreshable;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ItemTypeRefreshableTest {
    private static final ItemTypeRefreshable itemTypeRefreshable;

    static {
        SimplifiedAPI.enableDatabase();
        itemTypeRefreshable = SimplifiedAPI.getSqlRefreshable(ItemTypeRefreshable.class);
    }

    @Test
    public void getCachedListSync_ok() {
        List<ItemTypeModel> cachedList = Assertions.assertDoesNotThrow(itemTypeRefreshable::getCachedList);
        MatcherAssert.assertThat(cachedList.size(), Matchers.greaterThan(0));
    }
}
