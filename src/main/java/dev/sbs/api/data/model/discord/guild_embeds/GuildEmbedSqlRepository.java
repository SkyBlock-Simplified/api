package dev.sbs.api.data.model.discord.guild_embeds;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class GuildEmbedSqlRepository extends SqlRepository<GuildEmbedSqlModel> {

    public GuildEmbedSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
