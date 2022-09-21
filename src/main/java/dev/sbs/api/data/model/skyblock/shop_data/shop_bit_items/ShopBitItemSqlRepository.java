package dev.sbs.api.data.model.skyblock.shop_data.shop_bit_items;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class ShopBitItemSqlRepository extends SqlRepository<ShopBitItemSqlModel> {

    public ShopBitItemSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
