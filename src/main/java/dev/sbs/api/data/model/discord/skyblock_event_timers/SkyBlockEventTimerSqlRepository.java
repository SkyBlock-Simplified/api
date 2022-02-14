package dev.sbs.api.data.model.discord.skyblock_event_timers;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class SkyBlockEventTimerSqlRepository extends SqlRepository<SkyBlockEventTimerSqlModel> {

    public SkyBlockEventTimerSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
