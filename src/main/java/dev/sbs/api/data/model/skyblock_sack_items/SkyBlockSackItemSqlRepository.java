package dev.sbs.api.data.model.skyblock_sack_items;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class SkyBlockSackItemSqlRepository extends SqlRepository<SkyBlockSackItemSqlModel> {

    public SkyBlockSackItemSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
