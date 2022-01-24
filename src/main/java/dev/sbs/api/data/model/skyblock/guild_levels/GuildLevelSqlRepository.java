package dev.sbs.api.data.model.skyblock.guild_levels;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class GuildLevelSqlRepository extends SqlRepository<GuildLevelSqlModel> {

    public GuildLevelSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
