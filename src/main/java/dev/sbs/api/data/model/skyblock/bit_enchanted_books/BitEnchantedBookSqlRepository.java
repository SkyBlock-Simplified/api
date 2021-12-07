package dev.sbs.api.data.model.skyblock.bit_enchanted_books;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class BitEnchantedBookSqlRepository extends SqlRepository<BitEnchantedBookSqlModel> {

    public BitEnchantedBookSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
