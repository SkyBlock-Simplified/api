package dev.sbs.api.data.model.skyblock.bags;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class BagSqlRepository extends SqlRepository<BagSqlModel> {

    public BagSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
