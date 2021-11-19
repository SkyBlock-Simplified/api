package dev.sbs.api.data.model.skyblock.craftingtable_slots;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class CraftingTableSlotSqlRepository extends SqlRepository<CraftingTableSlotSqlModel> {

    public CraftingTableSlotSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
