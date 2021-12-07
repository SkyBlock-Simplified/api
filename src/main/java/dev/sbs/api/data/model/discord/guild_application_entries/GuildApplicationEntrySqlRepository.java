package dev.sbs.api.data.model.discord.guild_application_entries;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class GuildApplicationEntrySqlRepository extends SqlRepository<GuildApplicationEntrySqlModel> {

    public GuildApplicationEntrySqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
