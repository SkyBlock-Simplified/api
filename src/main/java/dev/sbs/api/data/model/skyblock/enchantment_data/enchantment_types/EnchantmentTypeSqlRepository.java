package dev.sbs.api.data.model.skyblock.enchantment_data.enchantment_types;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class EnchantmentTypeSqlRepository extends SqlRepository<EnchantmentTypeSqlModel> {

    public EnchantmentTypeSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
