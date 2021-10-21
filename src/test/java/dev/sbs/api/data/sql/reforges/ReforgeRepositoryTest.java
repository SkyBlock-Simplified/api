package dev.sbs.api.data.sql.reforges;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.sql.exception.SqlException;
import dev.sbs.api.data.sql.model.itemtypes.ItemTypeModel;
import dev.sbs.api.data.sql.model.itemtypes.ItemTypeRepository;
import dev.sbs.api.data.sql.model.rarities.RarityModel;
import dev.sbs.api.data.sql.model.rarities.RarityRepository;
import dev.sbs.api.data.sql.model.reforges.ReforgeModel;
import dev.sbs.api.data.sql.model.reforges.ReforgeRepository;
import dev.sbs.api.util.tuple.Pair;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ReforgeRepositoryTest {

    private static final ItemTypeRepository itemTypeRepository;
    private static final RarityRepository rarityRepository;
    private static final ReforgeRepository reforgeRepository;

    static {
        SimplifiedApi.enableDatabase();
        itemTypeRepository = SimplifiedApi.getSqlRepository(ItemTypeRepository.class);
        rarityRepository = SimplifiedApi.getSqlRepository(RarityRepository.class);
        reforgeRepository = SimplifiedApi.getSqlRepository(ReforgeRepository.class);
    }

    @Test
    public void findAll_ok() {
        List<ReforgeModel> reforges = reforgeRepository.findAll();
        MatcherAssert.assertThat(reforges.size(), Matchers.greaterThan(0));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getCachedList_ok() throws SqlException {
        ItemTypeModel sword = itemTypeRepository.findFirstOrNullCached(
                ItemTypeModel::getName, "Sword"
        );
        RarityModel legendary = rarityRepository.findFirstOrNullCached(
                RarityModel::getRarityTag, "LEGENDARY"
        );
        ReforgeModel spicy = reforgeRepository.findFirstOrNullCached(
                Pair.of(ReforgeModel::getItemType, sword),
                Pair.of(ReforgeModel::getRarity, legendary),
                Pair.of(ReforgeModel::getName, "Spicy")
        );
        MatcherAssert.assertThat(spicy, Matchers.notNullValue());
    }

}
