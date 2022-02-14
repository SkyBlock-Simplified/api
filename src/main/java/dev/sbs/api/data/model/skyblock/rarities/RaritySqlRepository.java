package dev.sbs.api.data.model.skyblock.rarities;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class RaritySqlRepository extends SqlRepository<RaritySqlModel> {

    public RaritySqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
