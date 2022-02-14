package dev.sbs.api.data.model.discord.users;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class UserSqlRepository extends SqlRepository<UserSqlModel> {

    public UserSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
