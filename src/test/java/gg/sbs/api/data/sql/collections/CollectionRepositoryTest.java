package gg.sbs.api.data.sql.collections;

import gg.sbs.api.SimplifiedApi;
import gg.sbs.api.data.sql.model.collections.CollectionModel;
import gg.sbs.api.data.sql.model.collections.CollectionRepository;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CollectionRepositoryTest {
    private static final CollectionRepository collectionRepository;

    static {
        SimplifiedApi.enableDatabase();
        collectionRepository = SimplifiedApi.getSqlRepository(CollectionRepository.class);
    }

    @Test
    public void findAll_ok() {
        List<CollectionModel> all = collectionRepository.findAll();
        MatcherAssert.assertThat(all.size(), Matchers.greaterThan(0));
    }
}
