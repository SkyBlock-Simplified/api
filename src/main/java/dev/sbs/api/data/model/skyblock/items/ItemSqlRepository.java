package dev.sbs.api.data.model.skyblock.items;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class ItemSqlRepository extends SqlRepository<ItemSqlModel> {

    public ItemSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
