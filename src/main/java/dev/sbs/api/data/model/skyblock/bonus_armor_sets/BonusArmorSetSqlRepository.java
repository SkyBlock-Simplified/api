package dev.sbs.api.data.model.skyblock.bonus_armor_sets;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class BonusArmorSetSqlRepository extends SqlRepository<BonusArmorSetSqlModel> {

    public BonusArmorSetSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
