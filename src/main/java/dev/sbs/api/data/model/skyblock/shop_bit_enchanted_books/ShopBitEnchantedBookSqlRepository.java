package dev.sbs.api.data.model.skyblock.shop_bit_enchanted_books;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class ShopBitEnchantedBookSqlRepository extends SqlRepository<ShopBitEnchantedBookSqlModel> {

    public ShopBitEnchantedBookSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
