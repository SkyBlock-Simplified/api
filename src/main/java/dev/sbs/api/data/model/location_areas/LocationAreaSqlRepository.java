package dev.sbs.api.data.model.location_areas;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class LocationAreaSqlRepository extends SqlRepository<LocationAreaSqlModel> {

    public LocationAreaSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
