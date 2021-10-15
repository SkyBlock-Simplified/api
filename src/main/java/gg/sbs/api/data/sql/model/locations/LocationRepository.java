package gg.sbs.api.data.sql.model.locations;

import gg.sbs.api.data.sql.SqlRepository;
import gg.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class LocationRepository extends SqlRepository<LocationModel> {

    public LocationRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public LocationRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}