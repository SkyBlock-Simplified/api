package dev.sbs.api.data.model.discord.command_data.command_categories;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.model.JsonModel;
import dev.sbs.api.data.model.discord.emojis.EmojiModel;
import dev.sbs.api.util.builder.hash.EqualsBuilder;
import dev.sbs.api.util.builder.hash.HashCodeBuilder;
import lombok.Getter;
import lombok.SneakyThrows;
import org.hibernate.annotations.UpdateTimestamp;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Table;
import java.time.Instant;

@Table(
    name = "discord_command_categories"
)
public class CommandCategoryJsonModel implements CommandCategoryModel, JsonModel {

    @Getter
    private Long id;

    @Getter
    private String key;

    @Getter
    private String name;

    @Getter
    private String description;

    @Getter
    @SerializedName("long_description")
    private String longDescription;

    @Getter
    @SerializedName("emoji_key")
    private String emojiKey;

    @Getter
    @UpdateTimestamp
    @SerializedName("updated_at")
    private @NotNull Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommandCategoryJsonModel that = (CommandCategoryJsonModel) o;

        return new EqualsBuilder()
            .append(this.getId(), that.getId())
            .append(this.getKey(), that.getKey())
            .append(this.getName(), that.getName())
            .append(this.getDescription(), that.getDescription())
            .append(this.getLongDescription(), that.getLongDescription())
            .append(this.getEmoji(), that.getEmoji())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getKey())
            .append(this.getName())
            .append(this.getDescription())
            .append(this.getLongDescription())
            .append(this.getEmoji())
            .append(this.getUpdatedAt())
            .build();
    }

    @SneakyThrows
    public EmojiModel getEmoji() {
        return SimplifiedApi.getRepositoryOf(EmojiModel.class).findFirstOrNull(EmojiModel::getKey, this.getEmojiKey());
    }

}
