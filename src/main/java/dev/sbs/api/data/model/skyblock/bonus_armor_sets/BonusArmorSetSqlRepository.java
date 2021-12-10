package dev.sbs.api.data.model.skyblock.bonus_armor_sets;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class BonusArmorSetSqlRepository extends SqlRepository<BonusArmorSetSqlModel> {

    public BonusArmorSetSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
