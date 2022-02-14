package dev.sbs.api.data.model.skyblock.enchantments;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class EnchantmentSqlRepository extends SqlRepository<EnchantmentSqlModel> {

    public EnchantmentSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
