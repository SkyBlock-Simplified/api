package dev.sbs.api.data.sql.model.accessories;

import dev.sbs.api.data.sql.SqlSession;
import dev.sbs.api.data.sql.SqlRepository;
import lombok.NonNull;

public class AccessoryRepository extends SqlRepository<AccessoryModel> {

    public AccessoryRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public AccessoryRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
