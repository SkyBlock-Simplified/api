package dev.sbs.api.data.model.skyblock.fairy_souls;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class FairySoulSqlRepository extends SqlRepository<FairySoulSqlModel> {

    public FairySoulSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
