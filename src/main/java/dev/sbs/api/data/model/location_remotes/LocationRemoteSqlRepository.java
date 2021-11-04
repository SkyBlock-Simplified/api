package dev.sbs.api.data.model.location_remotes;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class LocationRemoteSqlRepository extends SqlRepository<LocationRemoteSqlModel> {

    public LocationRemoteSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public LocationRemoteSqlRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
