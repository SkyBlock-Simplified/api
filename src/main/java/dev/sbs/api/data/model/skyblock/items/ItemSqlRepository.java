package dev.sbs.api.data.model.skyblock.items;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class ItemSqlRepository extends SqlRepository<ItemSqlModel> {

    public ItemSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
