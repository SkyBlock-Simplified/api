package dev.sbs.api.data.model.skyblock.location_data.location_areas;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class LocationAreaSqlRepository extends SqlRepository<LocationAreaSqlModel> {

    public LocationAreaSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
