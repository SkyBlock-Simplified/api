package dev.sbs.api.data.model.discord.guild_reports;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class GuildReportSqlRepository extends SqlRepository<GuildReportSqlModel> {

    public GuildReportSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
