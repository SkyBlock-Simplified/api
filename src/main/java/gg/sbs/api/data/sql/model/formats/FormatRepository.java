package gg.sbs.api.data.sql.model.formats;

import gg.sbs.api.data.sql.SqlRepository;
import gg.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class FormatRepository extends SqlRepository<FormatModel> {

    public FormatRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public FormatRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}