package dev.sbs.api.data.sql.reforges;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.Repository;
import dev.sbs.api.data.model.rarities.RarityModel;
import dev.sbs.api.data.model.reforge_stats.ReforgeStatModel;
import dev.sbs.api.data.model.reforge_types.ReforgeTypeModel;
import dev.sbs.api.data.model.reforges.ReforgeModel;
import dev.sbs.api.data.sql.exception.SqlException;
import dev.sbs.api.data.sql.function.FilterFunction;
import dev.sbs.api.util.tuple.Pair;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ReforgeRepositoryTest {

    private static final Repository<ReforgeTypeModel> reforgeTypeRepository;
    private static final Repository<RarityModel> rarityRepository;
    private static final Repository<ReforgeModel> reforgeRepository;
    private static final Repository<ReforgeStatModel> reforgeStatRepository;

    static {
        SimplifiedApi.enableDatabase();
        reforgeTypeRepository = SimplifiedApi.getRepositoryOf(ReforgeTypeModel.class);
        rarityRepository = SimplifiedApi.getRepositoryOf(RarityModel.class);
        reforgeRepository = SimplifiedApi.getRepositoryOf(ReforgeModel.class);
        reforgeStatRepository = SimplifiedApi.getRepositoryOf(ReforgeStatModel.class);
    }

    @Test
    public void findAll_ok() {
        try {
            List<ReforgeModel> reforges = reforgeRepository.findAll();
            MatcherAssert.assertThat(reforges.size(), Matchers.greaterThan(0));
        } catch (SqlException sqlException) {

        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getCachedList_ok() throws SqlException {
        ReforgeTypeModel sword = reforgeTypeRepository.findFirstOrNull(
                ReforgeTypeModel::getName, "Sword"
        );
        RarityModel legendary = rarityRepository.findFirstOrNull(
                RarityModel::getKey, "LEGENDARY"
        );
        ReforgeModel spicy = reforgeRepository.findFirstOrNull(
                Pair.of(ReforgeModel::getType, sword),
                Pair.of(ReforgeModel::getName, "Spicy")
        );
        ReforgeStatModel spicyStat = reforgeStatRepository.findFirstOrNull(
                Pair.of(FilterFunction.combine(ReforgeStatModel::getReforge, ReforgeModel::getKey), spicy.getKey()),
                Pair.of(ReforgeStatModel::getRarity, legendary)
        );
        MatcherAssert.assertThat(spicyStat, Matchers.notNullValue());
    }

}
