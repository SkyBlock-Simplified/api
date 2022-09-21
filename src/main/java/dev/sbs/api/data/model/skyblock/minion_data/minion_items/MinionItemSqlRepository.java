package dev.sbs.api.data.model.skyblock.minion_data.minion_items;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class MinionItemSqlRepository extends SqlRepository<MinionItemSqlModel> {

    public MinionItemSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
