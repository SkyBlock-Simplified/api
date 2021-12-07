package dev.sbs.api.data.model.discord.guild_reputation;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class GuildReputationSqlRepository extends SqlRepository<GuildReputationSqlModel> {

    public GuildReputationSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
