package dev.sbs.api.data.model.skyblock.potion_data.potion_brew_buffs;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class PotionBrewBuffSqlRepository extends SqlRepository<PotionBrewBuffSqlModel> {

    public PotionBrewBuffSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}