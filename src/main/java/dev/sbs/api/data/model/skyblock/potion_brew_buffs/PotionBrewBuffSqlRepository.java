package dev.sbs.api.data.model.skyblock.potion_brew_buffs;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class PotionBrewBuffSqlRepository extends SqlRepository<PotionBrewBuffSqlModel> {

    public PotionBrewBuffSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}