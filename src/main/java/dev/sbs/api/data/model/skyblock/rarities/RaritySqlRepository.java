package dev.sbs.api.data.model.skyblock.rarities;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class RaritySqlRepository extends SqlRepository<RaritySqlModel> {

    public RaritySqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
