package dev.sbs.api.data.sql.reforges;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.model.rarities.RaritySqlModel;
import dev.sbs.api.data.model.rarities.RaritySqlRepository;
import dev.sbs.api.data.model.reforge_stats.ReforgeStatModel;
import dev.sbs.api.data.model.reforge_stats.ReforgeStatSqlModel;
import dev.sbs.api.data.model.reforge_stats.ReforgeStatSqlRepository;
import dev.sbs.api.data.model.reforge_types.ReforgeTypeSqlModel;
import dev.sbs.api.data.model.reforge_types.ReforgeTypeSqlRepository;
import dev.sbs.api.data.model.reforges.ReforgeSqlModel;
import dev.sbs.api.data.model.reforges.ReforgeSqlRepository;
import dev.sbs.api.data.sql.exception.SqlException;
import dev.sbs.api.data.sql.function.FilterFunction;
import dev.sbs.api.util.tuple.Pair;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ReforgeRepositoryTest {

    private static final ReforgeTypeSqlRepository itemTypeRepository;
    private static final RaritySqlRepository rarityRepository;
    private static final ReforgeSqlRepository reforgeRepository;
    private static final ReforgeStatSqlRepository reforgeStatRepository;

    static {
        SimplifiedApi.enableDatabase();
        itemTypeRepository = SimplifiedApi.getSqlRepository(ReforgeTypeSqlRepository.class);
        rarityRepository = SimplifiedApi.getSqlRepository(RaritySqlRepository.class);
        reforgeRepository = SimplifiedApi.getSqlRepository(ReforgeSqlRepository.class);
        reforgeStatRepository = SimplifiedApi.getSqlRepository(ReforgeStatSqlRepository.class);
    }

    @Test
    public void findAll_ok() {
        try {
            List<ReforgeSqlModel> reforges = reforgeRepository.findAll();
            MatcherAssert.assertThat(reforges.size(), Matchers.greaterThan(0));
        } catch (SqlException sqlException) {

        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getCachedList_ok() throws SqlException {
        ReforgeTypeSqlModel sword = itemTypeRepository.findFirstOrNull(
                ReforgeTypeSqlModel::getName, "Sword"
        );
        RaritySqlModel legendary = rarityRepository.findFirstOrNull(
                RaritySqlModel::getKey, "LEGENDARY"
        );
        ReforgeSqlModel spicy = reforgeRepository.findFirstOrNull(
                Pair.of(ReforgeSqlModel::getType, sword),
                Pair.of(ReforgeSqlModel::getName, "Spicy")
        );
        ReforgeStatModel spicyStat = reforgeStatRepository.findFirstOrNull(
                Pair.of(FilterFunction.combine(ReforgeStatSqlModel::getReforge, ReforgeSqlModel::getKey), spicy.getKey()),
                Pair.of(ReforgeStatSqlModel::getRarity, legendary)
        );
        MatcherAssert.assertThat(spicyStat, Matchers.notNullValue());
    }

}
