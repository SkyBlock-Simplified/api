package dev.sbs.api.data.model.skyblock.bag_sizes;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class BagSizeSqlRepository extends SqlRepository<BagSizeSqlModel> {

    public BagSizeSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
