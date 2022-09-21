package dev.sbs.api.data.model.skyblock.craftingtable_data.craftingtable_slots;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class CraftingTableSlotSqlRepository extends SqlRepository<CraftingTableSlotSqlModel> {

    public CraftingTableSlotSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
