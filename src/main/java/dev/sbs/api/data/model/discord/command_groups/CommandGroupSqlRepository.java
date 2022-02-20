package dev.sbs.api.data.model.discord.command_groups;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class CommandGroupSqlRepository extends SqlRepository<CommandGroupSqlModel> {

    public CommandGroupSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
