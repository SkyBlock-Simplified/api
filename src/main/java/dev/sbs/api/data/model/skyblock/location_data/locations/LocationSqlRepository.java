package dev.sbs.api.data.model.skyblock.location_data.locations;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class LocationSqlRepository extends SqlRepository<LocationSqlModel> {

    public LocationSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
