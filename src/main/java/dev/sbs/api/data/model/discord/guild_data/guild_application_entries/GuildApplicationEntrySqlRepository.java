package dev.sbs.api.data.model.discord.guild_data.guild_application_entries;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class GuildApplicationEntrySqlRepository extends SqlRepository<GuildApplicationEntrySqlModel> {

    public GuildApplicationEntrySqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
