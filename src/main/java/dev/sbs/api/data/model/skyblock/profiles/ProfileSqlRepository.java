package dev.sbs.api.data.model.skyblock.profiles;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class ProfileSqlRepository extends SqlRepository<ProfileSqlModel> {

    public ProfileSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
