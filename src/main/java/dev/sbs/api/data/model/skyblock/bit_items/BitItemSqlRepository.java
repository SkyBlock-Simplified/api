package dev.sbs.api.data.model.skyblock.bit_items;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class BitItemSqlRepository extends SqlRepository<BitItemSqlModel> {

    public BitItemSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
