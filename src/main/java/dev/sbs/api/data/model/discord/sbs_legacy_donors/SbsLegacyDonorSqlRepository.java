package dev.sbs.api.data.model.discord.sbs_legacy_donors;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class SbsLegacyDonorSqlRepository extends SqlRepository<SbsLegacyDonorSqlModel> {

    public SbsLegacyDonorSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
