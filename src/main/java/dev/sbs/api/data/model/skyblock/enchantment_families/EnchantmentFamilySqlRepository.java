package dev.sbs.api.data.model.skyblock.enchantment_families;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class EnchantmentFamilySqlRepository extends SqlRepository<EnchantmentFamilySqlModel> {

    public EnchantmentFamilySqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
