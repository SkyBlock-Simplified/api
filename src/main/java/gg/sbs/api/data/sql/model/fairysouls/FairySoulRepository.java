package gg.sbs.api.data.sql.model.fairysouls;

import gg.sbs.api.data.sql.SqlRepository;
import gg.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class FairySoulRepository extends SqlRepository<FairySoulModel> {

    public FairySoulRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public FairySoulRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}