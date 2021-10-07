package gg.sbs.api.data.sql.items;

import gg.sbs.api.SimplifiedApi;
import gg.sbs.api.data.sql.model.items.ItemModel;
import gg.sbs.api.data.sql.model.items.ItemRepository;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ItemRepositoryTest {
    private static final ItemRepository itemRepository;

    static {
        SimplifiedApi.enableDatabase();
        itemRepository = SimplifiedApi.getSqlRepository(ItemRepository.class);
    }

    @Test
    public void findAll_ok() {
        List<ItemModel> all = itemRepository.findAll();
        MatcherAssert.assertThat(all.size(), Matchers.greaterThan(0));
    }
}
