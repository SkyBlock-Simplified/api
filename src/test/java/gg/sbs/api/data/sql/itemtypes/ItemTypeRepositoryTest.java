package gg.sbs.api.data.sql.itemtypes;

import gg.sbs.api.SimplifiedApi;
import gg.sbs.api.data.sql.model.itemtypes.ItemTypeModel;
import gg.sbs.api.data.sql.model.itemtypes.ItemTypeRepository;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ItemTypeRepositoryTest {
    private static final ItemTypeRepository itemTypeRepository;

    static {
        SimplifiedApi.enableDatabase();
        itemTypeRepository = SimplifiedApi.getSqlRepository(ItemTypeRepository.class);
    }

    @Test
    public void findAll_ok() {
        List<ItemTypeModel> all = itemTypeRepository.findAll();
        MatcherAssert.assertThat(all.size(), Matchers.greaterThan(0));
    }
}
