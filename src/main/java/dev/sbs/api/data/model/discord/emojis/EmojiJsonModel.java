package dev.sbs.api.data.model.discord.emojis;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.model.JsonModel;
import dev.sbs.api.data.model.discord.guild_data.guilds.GuildJsonModel;
import dev.sbs.api.util.builder.hash.EqualsBuilder;
import dev.sbs.api.util.builder.hash.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Table;
import java.time.Instant;

@Table(
    name = "discord_emojis"
)
public class EmojiJsonModel implements EmojiModel, JsonModel {

    @Getter
    private Long id;

    @Getter
    @SerializedName("emoji_id")
    private @NotNull Long emojiId;

    @Getter
    @SerializedName("guild_id")
    private @NotNull Long guildId;

    @Getter
    private String key;

    @Getter
    private String name;

    @Getter
    @Setter
    private boolean animated;

    @Getter
    @CreationTimestamp
    @SerializedName("submitted_at")
    private @NotNull Instant submittedAt;

    @Getter
    @UpdateTimestamp
    @SerializedName("updated_at")
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmojiJsonModel that)) return false;

        return new EqualsBuilder()
            .append(this.getId(), that.getId())
            .append(this.getGuild(), that.getGuild())
            .append(this.getKey(), that.getKey())
            .append(this.getName(), that.getName())
            .append(this.getEmojiId(), that.getEmojiId())
            .append(this.getSubmittedAt(), that.getSubmittedAt())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public GuildJsonModel getGuild() {
        return SimplifiedApi.getRepositoryOf(GuildJsonModel.class).findFirstOrNull(GuildJsonModel::getGuildId, this.guildId);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getGuild())
            .append(this.getKey())
            .append(this.getName())
            .append(this.getEmojiId())
            .append(this.getSubmittedAt())
            .append(this.getUpdatedAt())
            .build();
    }

}
