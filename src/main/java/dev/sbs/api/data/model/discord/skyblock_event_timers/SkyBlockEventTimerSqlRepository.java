package dev.sbs.api.data.model.discord.skyblock_event_timers;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class SkyBlockEventTimerSqlRepository extends SqlRepository<SkyBlockEventTimerSqlModel> {

    public SkyBlockEventTimerSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
