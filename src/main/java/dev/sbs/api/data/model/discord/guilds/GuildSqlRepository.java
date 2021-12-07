package dev.sbs.api.data.model.discord.guilds;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class GuildSqlRepository extends SqlRepository<GuildSqlModel> {

    public GuildSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
