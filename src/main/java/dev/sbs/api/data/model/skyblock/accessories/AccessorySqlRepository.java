package dev.sbs.api.data.model.skyblock.accessories;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class AccessorySqlRepository extends SqlRepository<AccessorySqlModel> {

    public AccessorySqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
