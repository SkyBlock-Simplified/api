package dev.sbs.api.data.model.skyblock.bag_sizes;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class BagSizeSqlRepository extends SqlRepository<BagSizeSqlModel> {

    public BagSizeSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
