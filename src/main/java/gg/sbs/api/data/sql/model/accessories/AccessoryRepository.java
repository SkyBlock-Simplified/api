package gg.sbs.api.data.sql.model.accessories;

import gg.sbs.api.data.sql.SqlRepository;
import gg.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class AccessoryRepository extends SqlRepository<AccessoryModel> {

    public AccessoryRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public AccessoryRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}