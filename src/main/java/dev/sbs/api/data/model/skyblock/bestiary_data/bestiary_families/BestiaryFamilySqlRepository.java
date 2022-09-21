package dev.sbs.api.data.model.skyblock.bestiary_data.bestiary_families;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class BestiaryFamilySqlRepository extends SqlRepository<BestiaryFamilySqlModel> {

    public BestiaryFamilySqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
