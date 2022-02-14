package dev.sbs.api.data.model.skyblock.bags;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class BagSqlRepository extends SqlRepository<BagSqlModel> {

    public BagSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
