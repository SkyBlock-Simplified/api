package dev.sbs.api.data.model.skyblock.bit_item_craftables;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class BitItemCraftableSqlRepository extends SqlRepository<BitItemCraftableSqlModel> {

    public BitItemCraftableSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
