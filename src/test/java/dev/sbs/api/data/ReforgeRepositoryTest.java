package dev.sbs.api.data;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.collection.search.SearchFunction;
import dev.sbs.api.data.model.skyblock.item_types.ItemTypeModel;
import dev.sbs.api.data.model.skyblock.rarities.RarityModel;
import dev.sbs.api.data.model.skyblock.reforge_data.reforge_stats.ReforgeStatModel;
import dev.sbs.api.data.model.skyblock.reforge_data.reforges.ReforgeModel;
import dev.sbs.api.data.sql.SqlConfig;
import dev.sbs.api.data.sql.exception.SqlException;
import dev.sbs.api.stream.pair.Pair;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ReforgeRepositoryTest {

    private static final Repository<ItemTypeModel> itemTypeRepository;
    private static final Repository<RarityModel> rarityRepository;
    private static final Repository<ReforgeModel> reforgeRepository;
    private static final Repository<ReforgeStatModel> reforgeStatRepository;

    static {
        SimplifiedApi.getSessionManager().connect(SqlConfig.defaultSql());
        itemTypeRepository = SimplifiedApi.getRepositoryOf(ItemTypeModel.class);
        rarityRepository = SimplifiedApi.getRepositoryOf(RarityModel.class);
        reforgeRepository = SimplifiedApi.getRepositoryOf(ReforgeModel.class);
        reforgeStatRepository = SimplifiedApi.getRepositoryOf(ReforgeStatModel.class);
    }

    @Test
    public void findAll_ok() {
        List<ReforgeModel> reforges = reforgeRepository.findAll();
        MatcherAssert.assertThat(reforges.size(), Matchers.greaterThan(0));
    }

    @Test
    public void getCachedList_ok() throws SqlException {
        ItemTypeModel sword = itemTypeRepository.findFirstOrNull(
                ItemTypeModel::getKey, "SWORD"
        );
        RarityModel legendary = rarityRepository.findFirstOrNull(
                RarityModel::getKey, "LEGENDARY"
        );
        ReforgeModel spicy = reforgeRepository.matchFirstOrNull(
            reforgeModel -> reforgeModel.getItemTypes().contains(sword.getKey()) &&
                reforgeModel.getKey().equals("SPICY")
        );
        ReforgeStatModel spicyStat = reforgeStatRepository.findFirstOrNull(
                Pair.of(SearchFunction.combine(ReforgeStatModel::getReforge, ReforgeModel::getKey), spicy.getKey()),
                Pair.of(ReforgeStatModel::getRarity, legendary)
        );
        MatcherAssert.assertThat(spicyStat, Matchers.notNullValue());
    }

}
