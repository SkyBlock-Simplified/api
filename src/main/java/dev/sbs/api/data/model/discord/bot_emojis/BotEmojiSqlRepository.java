package dev.sbs.api.data.model.discord.bot_emojis;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class BotEmojiSqlRepository extends SqlRepository<BotEmojiSqlModel> {

    public BotEmojiSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
