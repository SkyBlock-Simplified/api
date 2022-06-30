package dev.sbs.api.data.model.skyblock.accessory_powers;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class AccessoryPowerSqlRepository extends SqlRepository<AccessoryPowerSqlModel> {

    public AccessoryPowerSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
