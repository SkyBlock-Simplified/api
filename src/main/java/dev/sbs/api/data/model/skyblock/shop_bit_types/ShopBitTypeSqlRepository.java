package dev.sbs.api.data.model.skyblock.shop_bit_types;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class ShopBitTypeSqlRepository extends SqlRepository<ShopBitTypeSqlModel> {

    public ShopBitTypeSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
