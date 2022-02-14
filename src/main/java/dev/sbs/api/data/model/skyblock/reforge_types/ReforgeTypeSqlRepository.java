package dev.sbs.api.data.model.skyblock.reforge_types;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class ReforgeTypeSqlRepository extends SqlRepository<ReforgeTypeSqlModel> {

    public ReforgeTypeSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
