package dev.sbs.api.data.model.discord.guild_data.guild_report_types;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class GuildReportTypeSqlRepository extends SqlRepository<GuildReportTypeSqlModel> {

    public GuildReportTypeSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
