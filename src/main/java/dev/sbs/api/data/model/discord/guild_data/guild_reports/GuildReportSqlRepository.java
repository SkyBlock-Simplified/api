package dev.sbs.api.data.model.discord.guild_data.guild_reports;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class GuildReportSqlRepository extends SqlRepository<GuildReportSqlModel> {

    public GuildReportSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
