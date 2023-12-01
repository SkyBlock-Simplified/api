package dev.sbs.api.data.model.discord.emojis;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.discord.guild_data.guilds.GuildModel;

public interface EmojiModel extends Model {

    Long getEmojiId();

    GuildModel getGuild();

    String getKey();

    String getName();

    boolean isAnimated();

    default String getUrl() {
        return String.format("https://cdn.discordapp.com/emojis/%s.webp", this.getEmojiId());
    }

}
