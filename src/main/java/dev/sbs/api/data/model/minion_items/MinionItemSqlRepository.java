package dev.sbs.api.data.model.minion_items;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class MinionItemSqlRepository extends SqlRepository<MinionItemSqlModel> {

    public MinionItemSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
