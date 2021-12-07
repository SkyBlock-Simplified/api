package dev.sbs.api.data.model.skyblock.enchantment_stats;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class EnchantmentStatSqlRepository extends SqlRepository<EnchantmentStatSqlModel> {

    public EnchantmentStatSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
