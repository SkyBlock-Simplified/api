package dev.sbs.api.data.model.skyblock.reforge_data.reforges;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class ReforgeSqlRepository extends SqlRepository<ReforgeSqlModel> {

    public ReforgeSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
