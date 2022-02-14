package dev.sbs.api.data.model.skyblock.craftingtable_recipes;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class CraftingTableRecipeSqlRepository extends SqlRepository<CraftingTableRecipeSqlModel> {

    public CraftingTableRecipeSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
