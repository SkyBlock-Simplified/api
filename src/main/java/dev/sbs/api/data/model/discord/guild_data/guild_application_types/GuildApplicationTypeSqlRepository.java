package dev.sbs.api.data.model.discord.guild_data.guild_application_types;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class GuildApplicationTypeSqlRepository extends SqlRepository<GuildApplicationTypeSqlModel> {

    public GuildApplicationTypeSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
