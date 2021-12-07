package dev.sbs.api.data.model.skyblock.enchantment_types;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class EnchantmentTypeSqlRepository extends SqlRepository<EnchantmentTypeSqlModel> {

    public EnchantmentTypeSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
