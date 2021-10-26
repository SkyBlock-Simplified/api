package dev.sbs.api.model.sql.locations.locationareas;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class LocationAreaRepository extends SqlRepository<LocationAreaSqlModel> {

    public LocationAreaRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public LocationAreaRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
