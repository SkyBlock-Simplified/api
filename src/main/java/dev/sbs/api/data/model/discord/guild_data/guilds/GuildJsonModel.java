package dev.sbs.api.data.model.discord.guild_data.guilds;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.data.model.JsonModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Table;
import java.time.Instant;
import java.util.List;

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
    @SerializedName("developer_bot_enabled")
    private boolean developerBotEnabled;

    @Getter
    @Setter
    @SerializedName("reports_public")
    private boolean reportsPublic;

    @Getter
    @Setter
    @SerializedName("emoji_management")
    private boolean emojiServer;

    @Getter
    @Setter
    @SerializedName("admin_roles")
    private @NotNull List<Long> adminRoles = Concurrent.newList();

    @Getter
    @Setter
    @SerializedName("manager_roles")
    private @NotNull List<Long> managerRoles = Concurrent.newList();

    @Getter
    @Setter
    @SerializedName("mod_roles")
    private @NotNull List<Long> modRoles = Concurrent.newList();

    @Getter
    @Setter
    @SerializedName("helper_roles")
    private @NotNull List<Long> helperRoles = Concurrent.newList();

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
            .append(this.isDeveloperBotEnabled(), that.isDeveloperBotEnabled())
            .append(this.isReportsPublic(), that.isReportsPublic())
            .append(this.isEmojiServer(), that.isEmojiServer())
            .append(this.getId(), that.getId())
            .append(this.getGuildId(), that.getGuildId())
            .append(this.getName(), that.getName())
            .append(this.getAdminRoles(), that.getAdminRoles())
            .append(this.getManagerRoles(), that.getManagerRoles())
            .append(this.getModRoles(), that.getModRoles())
            .append(this.getHelperRoles(), that.getHelperRoles())
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
            .append(this.isDeveloperBotEnabled())
            .append(this.isReportsPublic())
            .append(this.isEmojiServer())
            .append(this.getAdminRoles())
            .append(this.getManagerRoles())
            .append(this.getModRoles())
            .append(this.getHelperRoles())
            .append(this.getSubmittedAt())
            .append(this.getUpdatedAt())
            .build();
    }

}
