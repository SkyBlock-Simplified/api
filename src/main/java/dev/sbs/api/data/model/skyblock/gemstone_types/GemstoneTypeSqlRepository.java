package dev.sbs.api.data.model.skyblock.gemstone_types;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class GemstoneTypeSqlRepository extends SqlRepository<GemstoneTypeSqlModel> {

    public GemstoneTypeSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
