package dev.sbs.api.data.model.skyblock.shop_bit_items;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class ShopBitItemSqlRepository extends SqlRepository<ShopBitItemSqlModel> {

    public ShopBitItemSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
