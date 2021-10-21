package dev.sbs.api.data.sql.model.locationareas;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class LocationAreaRepository extends SqlRepository<LocationAreaModel> {

    public LocationAreaRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public LocationAreaRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}