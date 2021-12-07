package dev.sbs.api.data.model.skyblock.profiles;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class ProfileSqlRepository extends SqlRepository<ProfileSqlModel> {

    public ProfileSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
