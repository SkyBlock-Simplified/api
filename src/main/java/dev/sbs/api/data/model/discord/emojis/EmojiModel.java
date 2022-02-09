package dev.sbs.api.data.model.discord.emojis;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.discord.guilds.GuildModel;
import dev.sbs.api.util.helper.FormatUtil;

import java.time.Instant;

public interface EmojiModel extends Model {

    Long getEmojiId();

    GuildModel getGuild();

    String getKey();

    String getName();

    boolean isAnimated();

    Instant getSubmittedAt();

    default String getUrl() {
        return FormatUtil.format("https://cdn.discordapp.com/emojis/{0}.webp", this.getEmojiId());
    }

}
