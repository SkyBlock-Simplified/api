package dev.sbs.api.data.model.skyblock_bag_sizes;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class SkyBlockBagSizeSqlRepository extends SqlRepository<SkyBlockBagSizeSqlModel> {

    public SkyBlockBagSizeSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
