package dev.sbs.api.data.model.discord.guild_applications;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class GuildApplicationSqlRepository extends SqlRepository<GuildApplicationSqlModel> {

    public GuildApplicationSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
