package dev.sbs.api.data.model.skyblock.bit_types;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class BitTypeSqlRepository extends SqlRepository<BitTypeSqlModel> {

    public BitTypeSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
