package dev.sbs.api.data.model.discord.emojis;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class EmojiSqlRepository extends SqlRepository<EmojiSqlModel> {

    public EmojiSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
