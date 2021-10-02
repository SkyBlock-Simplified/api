package gg.sbs.api.data.sql.itemtypes;

import gg.sbs.api.SimplifiedAPI;
import gg.sbs.api.data.sql.models.itemtypes.ItemTypeModel;
import gg.sbs.api.data.sql.models.itemtypes.ItemTypeRepository;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ItemTypeRepositoryTest {
    private static final ItemTypeRepository itemTypeRepository;

    static {
        SimplifiedAPI.enableDatabase();
        itemTypeRepository = SimplifiedAPI.getSqlRepository(ItemTypeRepository.class);
    }

    @Test
    public void findAll_ok() {
        List<ItemTypeModel> all = itemTypeRepository.findAll();
        MatcherAssert.assertThat(all.size(), Matchers.greaterThan(0));
    }
}
