package dev.sbs.api.data.model.discord.skyblock_events;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class SkyBlockEventSqlRepository extends SqlRepository<SkyBlockEventSqlModel> {

    public SkyBlockEventSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
