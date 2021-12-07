package dev.sbs.api.data.model.discord.guild_report_types;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class GuildReportTypeSqlRepository extends SqlRepository<GuildReportTypeSqlModel> {

    public GuildReportTypeSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
