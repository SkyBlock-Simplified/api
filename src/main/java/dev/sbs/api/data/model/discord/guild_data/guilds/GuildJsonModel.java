package dev.sbs.api.data.model.discord.guild_data.guilds;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.data.model.JsonModel;
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
    name = "discord_guilds"
)
public class GuildJsonModel implements GuildModel, JsonModel {

    @Getter
    private Long id;

    @Getter
    @Setter
    @SerializedName("guild_id")
    private @NotNull Long guildId;

    @Getter
    @Setter
    private @NotNull String name;

    @Getter
    @Setter
    @SerializedName("reports_public")
    private boolean reportsPublic;

    @Getter
    @Setter
    @SerializedName("emoji_management")
    private boolean emojiServer;

    @Getter
    @CreationTimestamp
    @SerializedName("submitted_at")
    private @NotNull Instant submittedAt;

    @Getter
    @UpdateTimestamp
    @SerializedName("updated_at")
    private @NotNull Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GuildJsonModel that = (GuildJsonModel) o;

        return new EqualsBuilder()
            .append(this.isReportsPublic(), that.isReportsPublic())
            .append(this.isEmojiServer(), that.isEmojiServer())
            .append(this.getId(), that.getId())
            .append(this.getGuildId(), that.getGuildId())
            .append(this.getName(), that.getName())
            .append(this.getSubmittedAt(), that.getSubmittedAt())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getGuildId())
            .append(this.getName())
            .append(this.isReportsPublic())
            .append(this.isEmojiServer())
            .append(this.getSubmittedAt())
            .append(this.getUpdatedAt())
            .build();
    }

}
