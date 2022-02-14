package dev.sbs.api.data.model.skyblock.slayers;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class SlayerSqlRepository extends SqlRepository<SlayerSqlModel> {

    public SlayerSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
