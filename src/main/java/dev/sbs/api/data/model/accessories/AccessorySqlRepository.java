package dev.sbs.api.data.model.accessories;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class AccessorySqlRepository extends SqlRepository<AccessorySqlModel> {

    public AccessorySqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
