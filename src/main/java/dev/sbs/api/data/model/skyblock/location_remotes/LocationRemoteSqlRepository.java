package dev.sbs.api.data.model.skyblock.location_remotes;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class LocationRemoteSqlRepository extends SqlRepository<LocationRemoteSqlModel> {

    public LocationRemoteSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
