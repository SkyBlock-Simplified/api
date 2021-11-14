package dev.sbs.api.data.model.formats;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class FormatSqlRepository extends SqlRepository<FormatSqlModel> {

    public FormatSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
