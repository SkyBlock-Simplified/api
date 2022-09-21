package dev.sbs.api.data.model.skyblock.minion_data.minion_uniques;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class MinionUniqueSqlRepository extends SqlRepository<MinionUniqueSqlModel> {

    public MinionUniqueSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
