package dev.sbs.api.data.model.skyblock.shop_profile_upgrades;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class ShopProfileUpgradeSqlRepository extends SqlRepository<ShopProfileUpgradeSqlModel> {

    public ShopProfileUpgradeSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
