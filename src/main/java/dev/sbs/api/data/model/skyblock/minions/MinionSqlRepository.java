package dev.sbs.api.data.model.skyblock.minions;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class MinionSqlRepository extends SqlRepository<MinionSqlModel> {

    public MinionSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
