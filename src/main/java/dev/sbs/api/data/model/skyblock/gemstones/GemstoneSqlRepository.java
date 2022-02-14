package dev.sbs.api.data.model.skyblock.gemstones;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class GemstoneSqlRepository extends SqlRepository<GemstoneSqlModel> {

    public GemstoneSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
