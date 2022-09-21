package dev.sbs.api.data.model.skyblock.bestiary_data.bestiary_type_levels;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class BestiaryTypeLevelSqlRepository extends SqlRepository<BestiaryTypeLevelSqlModel> {

    public BestiaryTypeLevelSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
