package dev.sbs.api.data.model.discord.application_requirements;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class ApplicationRequirementSqlRepository extends SqlRepository<ApplicationRequirementSqlModel> {

    public ApplicationRequirementSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
