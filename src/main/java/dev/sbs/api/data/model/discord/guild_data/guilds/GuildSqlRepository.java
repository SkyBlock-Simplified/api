package dev.sbs.api.data.model.discord.guild_data.guilds;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class GuildSqlRepository extends SqlRepository<GuildSqlModel> {

    public GuildSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
