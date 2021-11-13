package dev.sbs.api.data.model.skyblock_bags;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class SkyBlockBagSqlRepository extends SqlRepository<SkyBlockBagSqlModel> {

    public SkyBlockBagSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
