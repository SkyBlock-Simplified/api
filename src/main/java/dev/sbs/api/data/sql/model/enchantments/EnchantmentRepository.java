package dev.sbs.api.data.sql.model.enchantments;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class EnchantmentRepository extends SqlRepository<EnchantmentModel> {

    public EnchantmentRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public EnchantmentRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
