package dev.sbs.api.data.model.discord.emojis;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class EmojiSqlRepository extends SqlRepository<EmojiSqlModel> {

    public EmojiSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
