package dev.sbs.api.data.model.skyblock.accessory_data.accessories;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class AccessorySqlRepository extends SqlRepository<AccessorySqlModel> {

    public AccessorySqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
