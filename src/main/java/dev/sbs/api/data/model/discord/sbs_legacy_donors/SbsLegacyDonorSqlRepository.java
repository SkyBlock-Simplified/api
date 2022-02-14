package dev.sbs.api.data.model.discord.sbs_legacy_donors;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class SbsLegacyDonorSqlRepository extends SqlRepository<SbsLegacyDonorSqlModel> {

    public SbsLegacyDonorSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
