package gg.sbs.api.data.sql.reforges;

import gg.sbs.api.SimplifiedApi;
import gg.sbs.api.data.sql.exception.SqlException;
import gg.sbs.api.data.sql.model.itemtypes.ItemTypeModel;
import gg.sbs.api.data.sql.model.itemtypes.ItemTypeRepository;
import gg.sbs.api.data.sql.model.rarities.RarityModel;
import gg.sbs.api.data.sql.model.rarities.RarityRepository;
import gg.sbs.api.data.sql.model.reforges.ReforgeModel;
import gg.sbs.api.data.sql.model.reforges.ReforgeRepository;
import gg.sbs.api.util.Pair;
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
                new Pair<>(ReforgeModel::getItemType, sword),
                new Pair<>(ReforgeModel::getRarity, legendary),
                new Pair<>(ReforgeModel::getName, "Spicy")
        );
        MatcherAssert.assertThat(spicy, Matchers.notNullValue());
    }
}
