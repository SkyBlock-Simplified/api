package dev.sbs.api.data.model.discord.guild_embeds;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class GuildEmbedSqlRepository extends SqlRepository<GuildEmbedSqlModel> {

    public GuildEmbedSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
