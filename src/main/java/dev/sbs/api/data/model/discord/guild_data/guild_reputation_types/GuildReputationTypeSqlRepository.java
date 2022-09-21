package dev.sbs.api.data.model.discord.guild_data.guild_reputation_types;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class GuildReputationTypeSqlRepository extends SqlRepository<GuildReputationTypeSqlModel> {

    public GuildReputationTypeSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
