package gg.sbs.api.data.sql.rarities;

import gg.sbs.api.SimplifiedApi;
import gg.sbs.api.data.sql.exception.SqlException;
import gg.sbs.api.data.sql.models.rarities.RarityModel;
import gg.sbs.api.data.sql.models.rarities.RarityRefreshable;
import gg.sbs.api.util.Pair;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class RarityRefreshableTest {
    private static final RarityRefreshable rarityRefreshable;

    static {
        SimplifiedApi.enableDatabase();
        rarityRefreshable = SimplifiedApi.getSqlRefreshable(gg.sbs.api.data.sql.models.rarities.RarityRefreshable.class);
    }

    @Test
    public void getCachedList_ok() throws SqlException {
        RarityModel legendary = rarityRefreshable.findFirstOrNull(
                new Pair<>(RarityModel::getHypixelName, "LEGENDARY"),
                new Pair<>(RarityModel::isHasHypixelName, Boolean.valueOf(true))
        );;
        MatcherAssert.assertThat(legendary, Matchers.notNullValue());
    }
}
