package dev.sbs.api.data.model.discord.guild_embeds;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.discord.embed_types.EmbedTypeModel;
import dev.sbs.api.data.model.discord.guilds.GuildModel;

import java.awt.*;
import java.time.Instant;

public interface GuildEmbedModel extends Model {

    GuildModel getGuild();

    EmbedTypeModel getEmbedType();

    Color getColor();

    String getTitle();

    String getUrl();

    String getDescription();

    String getAuthorName();

    String getAuthorUrl();

    String getAuthorIconUrl();

    String getImageUrl();

    String getThumbnailUrl();

    String getVideoUrl();

    Instant getTimestamp();

    String getFooterText();

    String getFooterIconUrl();

    String getNotes();

    Long getSubmitterDiscordId();

    Long getEditorDiscordId();

    Instant getSubmittedAt();

}
