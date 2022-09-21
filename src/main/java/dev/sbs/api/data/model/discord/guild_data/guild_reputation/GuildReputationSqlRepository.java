package dev.sbs.api.data.model.discord.guild_data.guild_reputation;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class GuildReputationSqlRepository extends SqlRepository<GuildReputationSqlModel> {

    public GuildReputationSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
