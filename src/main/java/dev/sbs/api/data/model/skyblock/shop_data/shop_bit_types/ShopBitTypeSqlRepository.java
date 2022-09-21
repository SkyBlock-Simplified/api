package dev.sbs.api.data.model.skyblock.shop_data.shop_bit_types;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class ShopBitTypeSqlRepository extends SqlRepository<ShopBitTypeSqlModel> {

    public ShopBitTypeSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
