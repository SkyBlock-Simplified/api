package dev.sbs.api.data.model.skyblock.shop_bit_item_craftables;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class ShopBitItemCraftableSqlRepository extends SqlRepository<ShopBitItemCraftableSqlModel> {

    public ShopBitItemCraftableSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
