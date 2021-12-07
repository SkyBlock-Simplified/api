package dev.sbs.api.data.model.discord.skyblock_events;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class SkyBlockEventSqlRepository extends SqlRepository<SkyBlockEventSqlModel> {

    public SkyBlockEventSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
