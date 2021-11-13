package dev.sbs.api.data.model.slayers;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class SlayerSqlRepository extends SqlRepository<SlayerSqlModel> {

    public SlayerSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
