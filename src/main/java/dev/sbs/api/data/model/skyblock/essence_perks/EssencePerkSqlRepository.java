package dev.sbs.api.data.model.skyblock.essence_perks;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class EssencePerkSqlRepository extends SqlRepository<EssencePerkSqlModel> {

    public EssencePerkSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
