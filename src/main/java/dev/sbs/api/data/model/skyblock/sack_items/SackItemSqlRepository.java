package dev.sbs.api.data.model.skyblock.sack_items;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class SackItemSqlRepository extends SqlRepository<SackItemSqlModel> {

    public SackItemSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
