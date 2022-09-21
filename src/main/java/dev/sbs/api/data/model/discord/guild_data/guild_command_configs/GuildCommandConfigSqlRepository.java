package dev.sbs.api.data.model.discord.guild_data.guild_command_configs;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class GuildCommandConfigSqlRepository extends SqlRepository<GuildCommandConfigSqlModel> {

    public GuildCommandConfigSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
