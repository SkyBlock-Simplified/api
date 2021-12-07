package dev.sbs.api.data.model.discord.guild_command_configs;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class GuildCommandConfigSqlRepository extends SqlRepository<GuildCommandConfigSqlModel> {

    public GuildCommandConfigSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
