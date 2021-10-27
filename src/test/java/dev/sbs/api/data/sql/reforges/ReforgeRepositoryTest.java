package dev.sbs.api.data.sql.reforges;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.sql.exception.SqlException;
import dev.sbs.api.model.sql.reforges.reforgecategories.ReforgeCategorySqlModel;
import dev.sbs.api.model.sql.reforges.reforgecategories.ReforgeCategoryRepository;
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

    private static final ReforgeCategoryRepository itemTypeRepository;
    private static final RarityRepository rarityRepository;
    private static final ReforgeRepository reforgeRepository;

    static {
        SimplifiedApi.enableDatabase();
        itemTypeRepository = SimplifiedApi.getSqlRepository(ReforgeCategoryRepository.class);
        rarityRepository = SimplifiedApi.getSqlRepository(RarityRepository.class);
        reforgeRepository = SimplifiedApi.getSqlRepository(ReforgeRepository.class);
    }

    @Test
    public void findAll_ok() {
        List<ReforgeSqlModel> reforges = reforgeRepository.findAll();
        MatcherAssert.assertThat(reforges.size(), Matchers.greaterThan(0));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getCachedList_ok() throws SqlException {
        ReforgeCategorySqlModel sword = itemTypeRepository.findFirstOrNullCached(
                ReforgeCategorySqlModel::getName, "Sword"
        );
        RaritySqlModel legendary = rarityRepository.findFirstOrNullCached(
                RaritySqlModel::getKey, "LEGENDARY"
        );
        ReforgeSqlModel spicy = reforgeRepository.findFirstOrNullCached(
                Pair.of(ReforgeSqlModel::getItemType, sword),
                Pair.of(ReforgeSqlModel::getRarity, legendary),
                Pair.of(ReforgeSqlModel::getName, "Spicy")
        );
        MatcherAssert.assertThat(spicy, Matchers.notNullValue());
    }

}
