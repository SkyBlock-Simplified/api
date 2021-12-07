package dev.sbs.api.data.model.skyblock.enchantment_families;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class EnchantmentFamilySqlRepository extends SqlRepository<EnchantmentFamilySqlModel> {

    public EnchantmentFamilySqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
