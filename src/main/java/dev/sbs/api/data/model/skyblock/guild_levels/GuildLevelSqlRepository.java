package dev.sbs.api.data.model.skyblock.guild_levels;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class GuildLevelSqlRepository extends SqlRepository<GuildLevelSqlModel> {

    public GuildLevelSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
