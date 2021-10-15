package gg.sbs.api.data.sql.model.locationareas;

import gg.sbs.api.data.sql.SqlRepository;
import gg.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class LocationAreaRepository extends SqlRepository<LocationAreaModel> {

    public LocationAreaRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public LocationAreaRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}