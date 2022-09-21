package dev.sbs.api.data.model.skyblock.bestiary_data.bestiary_types;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class BestiaryTypeSqlRepository extends SqlRepository<BestiaryTypeSqlModel> {

    public BestiaryTypeSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
