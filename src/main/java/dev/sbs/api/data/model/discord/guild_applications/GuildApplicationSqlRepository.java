package dev.sbs.api.data.model.discord.guild_applications;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class GuildApplicationSqlRepository extends SqlRepository<GuildApplicationSqlModel> {

    public GuildApplicationSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
