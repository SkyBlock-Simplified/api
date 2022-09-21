package dev.sbs.api.data.model.skyblock.shop_data.shop_bit_enchanted_books;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class ShopBitEnchantedBookSqlRepository extends SqlRepository<ShopBitEnchantedBookSqlModel> {

    public ShopBitEnchantedBookSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
