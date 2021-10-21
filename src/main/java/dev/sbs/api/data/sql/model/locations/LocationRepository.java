package dev.sbs.api.data.sql.model.locations;

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
