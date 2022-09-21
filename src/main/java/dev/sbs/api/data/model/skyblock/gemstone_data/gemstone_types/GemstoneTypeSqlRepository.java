package dev.sbs.api.data.model.skyblock.gemstone_data.gemstone_types;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class GemstoneTypeSqlRepository extends SqlRepository<GemstoneTypeSqlModel> {

    public GemstoneTypeSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
