package dev.sbs.api.data.model.discord.command_data.command_configs;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.model.JsonModel;
import dev.sbs.api.data.model.discord.command_data.command_categories.CommandCategoryJsonModel;
import dev.sbs.api.data.model.discord.command_data.command_groups.CommandGroupJsonModel;
import dev.sbs.api.data.model.discord.command_data.command_parents.CommandParentJsonModel;
import dev.sbs.api.data.model.discord.emojis.EmojiJsonModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
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
public class CommandConfigJsonModel implements CommandConfigModel, JsonModel {

    @Getter
    private Long id;

    @Getter
    @Setter
    @SerializedName("identifier")
    private UUID uniqueId;

    @SerializedName("parent_key")
    private @Nullable String parentKey;

    @SerializedName("group_key")
    private @Nullable String groupKey;

    @Getter
    @Setter
    private @NotNull String name;

    @Getter
    @Setter
    private @NotNull String description;

    @Getter
    @Setter
    @SerializedName("long_description")
    private @Nullable String longDescription;

    @Getter
    @Setter
    @SerializedName("emoji_key")
    private @Nullable String emojiKey;

    @Getter
    @Setter
    @SerializedName("category_key")
    private @Nullable String categoryKey;

    @Getter
    @Setter
    @SerializedName("developer_only")
    private boolean developerOnly;

    @Getter
    @Setter
    private boolean enabled;

    @Getter
    @Setter
    @SerializedName("inherit_permissions")
    private boolean inheritingPermissions;

    @Getter
    @Setter
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

        CommandConfigJsonModel that = (CommandConfigJsonModel) o;

        return new EqualsBuilder()
            .append(this.isDeveloperOnly(), that.isDeveloperOnly())
            .append(this.isEnabled(), that.isEnabled())
            .append(this.isInheritingPermissions(), that.isInheritingPermissions())
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
    public CommandCategoryJsonModel getCategory() {
        return SimplifiedApi.getRepositoryOf(CommandCategoryJsonModel.class).findFirstOrNull(CommandCategoryJsonModel::getKey, this.categoryKey);
    }

    @Override
    public EmojiJsonModel getEmoji() {
        return SimplifiedApi.getRepositoryOf(EmojiJsonModel.class).findFirstOrNull(EmojiJsonModel::getKey, this.emojiKey);
    }

    @Override
    public CommandGroupJsonModel getGroup() {
        return SimplifiedApi.getRepositoryOf(CommandGroupJsonModel.class).findFirstOrNull(CommandGroupJsonModel::getKey, this.groupKey);
    }

    @Override
    public CommandParentJsonModel getParent() {
        return SimplifiedApi.getRepositoryOf(CommandParentJsonModel.class).findFirstOrNull(CommandParentJsonModel::getKey, this.parentKey);
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
            .append(this.isInheritingPermissions())
            .append(this.getStatus())
            .append(this.getSubmittedAt())
            .append(this.getUpdatedAt())
            .build();
    }

    public void setCategory(CommandCategoryJsonModel commandCategoryJsonModel) {
        this.categoryKey = commandCategoryJsonModel == null ? null : commandCategoryJsonModel.getKey();
    }

    public void setEmoji(EmojiJsonModel emojiJsonModel) {
        this.emojiKey = emojiJsonModel == null ? null : emojiJsonModel.getKey();
    }

    public void setGroup(CommandGroupJsonModel commandGroupJsonModel) {
        this.groupKey = commandGroupJsonModel == null ? null : commandGroupJsonModel.getKey();
    }

    public void setParent(CommandParentJsonModel commandParentJsonModel) {
        this.parentKey = commandParentJsonModel == null ? null : commandParentJsonModel.getKey();
    }

}
