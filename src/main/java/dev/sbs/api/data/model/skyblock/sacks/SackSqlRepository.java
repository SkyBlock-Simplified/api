package dev.sbs.api.data.model.skyblock.sacks;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class SackSqlRepository extends SqlRepository<SackSqlModel> {

    public SackSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
