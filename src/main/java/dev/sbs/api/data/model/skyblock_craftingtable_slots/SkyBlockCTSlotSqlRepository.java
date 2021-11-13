package dev.sbs.api.data.model.skyblock_craftingtable_slots;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class SkyBlockCTSlotSqlRepository extends SqlRepository<SkyBlockCTSlotSqlModel> {

    public SkyBlockCTSlotSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
