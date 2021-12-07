package dev.sbs.api.data.model.discord.guild_skyblock_events;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class GuildSkyBlockEventSqlRepository extends SqlRepository<GuildSkyBlockEventSqlModel> {

    public GuildSkyBlockEventSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
