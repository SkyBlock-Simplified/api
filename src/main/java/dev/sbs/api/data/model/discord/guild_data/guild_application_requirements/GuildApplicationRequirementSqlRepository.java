package dev.sbs.api.data.model.discord.guild_data.guild_application_requirements;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class GuildApplicationRequirementSqlRepository extends SqlRepository<GuildApplicationRequirementSqlModel> {

    public GuildApplicationRequirementSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
