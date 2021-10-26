package dev.sbs.api.model.sql.accessories;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class AccessoryRepository extends SqlRepository<AccessorySqlModel> {

    public AccessoryRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public AccessoryRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
