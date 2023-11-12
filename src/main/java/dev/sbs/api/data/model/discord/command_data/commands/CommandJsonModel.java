package dev.sbs.api.data.model.discord.command_data.commands;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.model.JsonModel;
import dev.sbs.api.data.model.discord.command_data.command_categories.CommandCategoryModel;
import dev.sbs.api.data.model.discord.command_data.command_groups.CommandGroupModel;
import dev.sbs.api.data.model.discord.command_data.command_parents.CommandParentModel;
import dev.sbs.api.data.model.discord.emojis.EmojiModel;
import dev.sbs.api.data.model.discord.guild_data.guilds.GuildModel;
import dev.sbs.api.util.builder.hash.EqualsBuilder;
import dev.sbs.api.util.builder.hash.HashCodeBuilder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.Column;
import javax.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Table(
    name = "discord_command_configs"
)
public class CommandJsonModel implements CommandModel, JsonModel {

    @Getter
    private Long id;

    @Getter
    @SerializedName("identifier")
    private UUID uniqueId;

    @Getter
    @SerializedName("parent_key")
    private @Nullable String parentKey;

    @Getter
    @SerializedName("group_key")
    private @Nullable String groupKey;

    @Getter
    private String name;

    @Getter
    private @Nullable Long guildId;

    @Getter
    @SerializedName("emoji_key")
    private @Nullable String emojiKey;

    @Getter
    @SerializedName("category_key")
    private @Nullable String categoryKey;

    @Getter
    private String description;

    @Getter
    @SerializedName("long_description")
    private @Nullable String longDescription;

    @Getter
    private Long defaultMemberPermissions;

    @Getter
    private boolean nsfw;

    @Getter
    private boolean privateChannelEnabled;

    @Getter
    @SerializedName("developer_only")
    private boolean developerOnly;

    @Getter
    private boolean enabled;

    @Getter
    @Column(name = "status")
    private String status;

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

        CommandJsonModel that = (CommandJsonModel) o;

        return new EqualsBuilder()
            .append(this.isDeveloperOnly(), that.isDeveloperOnly())
            .append(this.isEnabled(), that.isEnabled())
            .append(this.getId(), that.getId())
            .append(this.getUniqueId(), that.getUniqueId())
            .append(this.getParent(), that.getParent())
            .append(this.getGroup(), that.getGroup())
            .append(this.getName(), that.getName())
            .append(this.getDescription(), that.getDescription())
            .append(this.getLongDescription(), that.getLongDescription())
            .append(this.getEmoji(), that.getEmoji())
            .append(this.getCategory(), that.getCategory())
            .append(this.getStatus(), that.getStatus())
            .append(this.getSubmittedAt(), that.getSubmittedAt())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public CommandCategoryModel getCategory() {
        return SimplifiedApi.getRepositoryOf(CommandCategoryModel.class).findFirstOrNull(CommandCategoryModel::getKey, this.getCategoryKey());
    }

    @Override
    public EmojiModel getEmoji() {
        return SimplifiedApi.getRepositoryOf(EmojiModel.class).findFirstOrNull(EmojiModel::getKey, this.getEmojiKey());
    }

    @Override
    public CommandParentModel getParent() {
        return SimplifiedApi.getRepositoryOf(CommandParentModel.class).findFirstOrNull(CommandParentModel::getKey, this.getParentKey());
    }

    @Override
    public CommandGroupModel getGroup() {
        return SimplifiedApi.getRepositoryOf(CommandGroupModel.class).findFirstOrNull(CommandGroupModel::getKey, this.getGroupKey());
    }

    @Override
    public GuildModel getGuild() {
        return SimplifiedApi.getRepositoryOf(GuildModel.class).findFirstOrNull(GuildModel::getGuildId, this.getGuildId());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getUniqueId())
            .append(this.getParent())
            .append(this.getGroup())
            .append(this.getName())
            .append(this.getDescription())
            .append(this.getLongDescription())
            .append(this.getEmoji())
            .append(this.getCategory())
            .append(this.isDeveloperOnly())
            .append(this.isEnabled())
            .append(this.getStatus())
            .append(this.getSubmittedAt())
            .append(this.getUpdatedAt())
            .build();
    }

}
