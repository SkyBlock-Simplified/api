package dev.sbs.api.data.model.skyblock.locations;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class LocationSqlRepository extends SqlRepository<LocationSqlModel> {

    public LocationSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
