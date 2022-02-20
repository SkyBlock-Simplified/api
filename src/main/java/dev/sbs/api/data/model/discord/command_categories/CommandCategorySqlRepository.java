package dev.sbs.api.data.model.discord.command_categories;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class CommandCategorySqlRepository extends SqlRepository<CommandCategorySqlModel> {

    public CommandCategorySqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
