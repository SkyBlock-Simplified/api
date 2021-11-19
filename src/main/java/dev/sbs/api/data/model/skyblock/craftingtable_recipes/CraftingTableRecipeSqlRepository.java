package dev.sbs.api.data.model.skyblock.craftingtable_recipes;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class CraftingTableRecipeSqlRepository extends SqlRepository<CraftingTableRecipeSqlModel> {

    public CraftingTableRecipeSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
