package dev.sbs.api.data.model.skyblock.enchantment_stats;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class EnchantmentStatSqlRepository extends SqlRepository<EnchantmentStatSqlModel> {

    public EnchantmentStatSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
