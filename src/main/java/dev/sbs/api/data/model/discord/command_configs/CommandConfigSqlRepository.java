package dev.sbs.api.data.model.discord.command_configs;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class CommandConfigSqlRepository extends SqlRepository<CommandConfigSqlModel> {

    public CommandConfigSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
