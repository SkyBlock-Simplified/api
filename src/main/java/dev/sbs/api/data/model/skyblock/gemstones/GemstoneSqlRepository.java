package dev.sbs.api.data.model.skyblock.gemstones;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class GemstoneSqlRepository extends SqlRepository<GemstoneSqlModel> {

    public GemstoneSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
