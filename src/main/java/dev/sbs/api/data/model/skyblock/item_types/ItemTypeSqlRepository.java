package dev.sbs.api.data.model.skyblock.item_types;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class ItemTypeSqlRepository extends SqlRepository<ItemTypeSqlModel> {

    public ItemTypeSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
