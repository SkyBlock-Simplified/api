package dev.sbs.api.data.sql.reforges;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.sql.exception.SqlException;
import dev.sbs.api.model.ReforgeStatModel;
import dev.sbs.api.model.sql.reforges.reforgestats.ReforgeStatRepository;
import dev.sbs.api.model.sql.reforges.reforgestats.ReforgeStatSqlModel;
import dev.sbs.api.model.sql.reforges.reforgetypes.ReforgeTypeSqlModel;
import dev.sbs.api.model.sql.reforges.reforgetypes.ReforgeTypeRepository;
import dev.sbs.api.model.sql.rarities.RaritySqlModel;
import dev.sbs.api.model.sql.rarities.RarityRepository;
import dev.sbs.api.model.sql.reforges.ReforgeSqlModel;
import dev.sbs.api.model.sql.reforges.ReforgeRepository;
import dev.sbs.api.util.tuple.Pair;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ReforgeRepositoryTest {

    private static final ReforgeTypeRepository itemTypeRepository;
    private static final RarityRepository rarityRepository;
    private static final ReforgeRepository reforgeRepository;
    private static final ReforgeStatRepository reforgeStatRepository;

    static {
        SimplifiedApi.enableDatabase();
        itemTypeRepository = SimplifiedApi.getSqlRepository(ReforgeTypeRepository.class);
        rarityRepository = SimplifiedApi.getSqlRepository(RarityRepository.class);
        reforgeRepository = SimplifiedApi.getSqlRepository(ReforgeRepository.class);
        reforgeStatRepository = SimplifiedApi.getSqlRepository(ReforgeStatRepository.class);
    }

    @Test
    public void findAll_ok() {
        List<ReforgeSqlModel> reforges = reforgeRepository.findAll();
        MatcherAssert.assertThat(reforges.size(), Matchers.greaterThan(0));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getCachedList_ok() throws SqlException {
        ReforgeTypeSqlModel sword = itemTypeRepository.findFirstOrNullCached(
                ReforgeTypeSqlModel::getName, "Sword"
        );
        RaritySqlModel legendary = rarityRepository.findFirstOrNullCached(
                RaritySqlModel::getKey, "LEGENDARY"
        );
        ReforgeSqlModel spicy = reforgeRepository.findFirstOrNullCached(
                Pair.of(ReforgeSqlModel::getType, sword),
                Pair.of(ReforgeSqlModel::getName, "Spicy")
        );
        ReforgeStatModel spicyStat = reforgeStatRepository.findFirstOrNullCached(
                Pair.of(ReforgeStatSqlModel::getKey, spicy.getKey()),
                Pair.of(ReforgeStatSqlModel::getRarity, legendary)
        );
        MatcherAssert.assertThat(spicyStat, Matchers.notNullValue());
    }

}
