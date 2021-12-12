package dev.sbs.api.data.model.skyblock.shop_bit_item_craftables;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class ShopBitItemCraftableSqlRepository extends SqlRepository<ShopBitItemCraftableSqlModel> {

    public ShopBitItemCraftableSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
