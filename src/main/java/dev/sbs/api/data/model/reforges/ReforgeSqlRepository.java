package dev.sbs.api.data.model.reforges;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class ReforgeSqlRepository extends SqlRepository<ReforgeSqlModel> {

    public ReforgeSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
