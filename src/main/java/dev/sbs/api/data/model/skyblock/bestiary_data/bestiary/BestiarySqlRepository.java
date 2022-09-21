package dev.sbs.api.data.model.skyblock.bestiary_data.bestiary;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class BestiarySqlRepository extends SqlRepository<BestiarySqlModel> {

    public BestiarySqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
