package dev.sbs.api.data.model.skyblock.craftingtable_recipe_slots;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class CraftingTableRecipeSlotSqlRepository extends SqlRepository<CraftingTableRecipeSlotSqlModel> {

    public CraftingTableRecipeSlotSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}