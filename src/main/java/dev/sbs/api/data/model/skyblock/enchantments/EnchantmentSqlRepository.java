package dev.sbs.api.data.model.skyblock.enchantments;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class EnchantmentSqlRepository extends SqlRepository<EnchantmentSqlModel> {

    public EnchantmentSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
