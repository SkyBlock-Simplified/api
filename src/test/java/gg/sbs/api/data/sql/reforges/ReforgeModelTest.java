package gg.sbs.api.data.sql.reforges;

import gg.sbs.api.SimplifiedAPI;
import gg.sbs.api.data.sql.SqlException;
import gg.sbs.api.data.sql.models.rarities.RarityModel;
import gg.sbs.api.data.sql.models.rarities.RarityRefreshable;
import gg.sbs.api.data.sql.models.reforges.ReforgeModel;
import gg.sbs.api.data.sql.models.reforges.ReforgeRefreshable;
import gg.sbs.api.util.Pair;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class ReforgeModelTest {
    private static final RarityRefreshable rarityRefreshable;
    private static final ReforgeRefreshable reforgeRefreshable;

    static {
        SimplifiedAPI.enableDatabase();
        rarityRefreshable = SimplifiedAPI.getSqlRefreshable(RarityRefreshable.class);
        reforgeRefreshable = SimplifiedAPI.getSqlRefreshable(ReforgeRefreshable.class);
    }

    @Test
    public void getEffectNum_ok() throws SqlException {
        RarityModel uncommon = rarityRefreshable.findFirstOrNull(
                RarityModel::getName, "Uncommon"
        );
        ReforgeModel uncommonVivid = reforgeRefreshable.findFirstOrNull(
                new Pair<>(ReforgeModel::getName, "Vivid"),
                new Pair<>(ReforgeModel::getRarity, uncommon)
        );
        MatcherAssert.assertThat(uncommonVivid, Matchers.notNullValue());
        double speed = uncommonVivid.getEffectNum("spd");
        MatcherAssert.assertThat(speed, Matchers.equalTo(2.));
    }
}
