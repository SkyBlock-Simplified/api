package dev.sbs.api.model.sql.locations;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class LocationRepository extends SqlRepository<LocationModel> {

    public LocationRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public LocationRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
