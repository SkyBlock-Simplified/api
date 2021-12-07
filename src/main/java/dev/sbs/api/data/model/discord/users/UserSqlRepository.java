package dev.sbs.api.data.model.discord.users;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class UserSqlRepository extends SqlRepository<UserSqlModel> {

    public UserSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
