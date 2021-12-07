package dev.sbs.api.data.model.discord.guild_reputation_types;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class GuildReputationTypeSqlRepository extends SqlRepository<GuildReputationTypeSqlModel> {

    public GuildReputationTypeSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
