package dev.sbs.api.data.model.discord.command_configs;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class CommandConfigSqlRepository extends SqlRepository<CommandConfigSqlModel> {

    public CommandConfigSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
