package dev.sbs.api.data.model.skyblock.sack_items;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class SackItemSqlRepository extends SqlRepository<SackItemSqlModel> {

    public SackItemSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
