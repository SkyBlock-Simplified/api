package dev.sbs.api.data.model.skyblock.armor_set_bonus;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class ArmorSetBonusSqlRepository extends SqlRepository<ArmorSetBonusSqlModel> {

    public ArmorSetBonusSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
