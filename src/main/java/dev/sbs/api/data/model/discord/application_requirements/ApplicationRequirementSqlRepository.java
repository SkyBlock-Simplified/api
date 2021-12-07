package dev.sbs.api.data.model.discord.application_requirements;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class ApplicationRequirementSqlRepository extends SqlRepository<ApplicationRequirementSqlModel> {

    public ApplicationRequirementSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
