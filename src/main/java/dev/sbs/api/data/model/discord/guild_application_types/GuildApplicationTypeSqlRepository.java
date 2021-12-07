package dev.sbs.api.data.model.discord.guild_application_types;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class GuildApplicationTypeSqlRepository extends SqlRepository<GuildApplicationTypeSqlModel> {

    public GuildApplicationTypeSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
