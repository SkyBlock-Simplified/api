package dev.sbs.api.data.model.skyblock.bonus_enchantment_stats;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class BonusEnchantmentStatSqlRepository extends SqlRepository<BonusEnchantmentStatSqlModel> {

    public BonusEnchantmentStatSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
