package dev.sbs.api.data.model.reforge_types;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class ReforgeTypeSqlRepository extends SqlRepository<ReforgeTypeSqlModel> {

    public ReforgeTypeSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
