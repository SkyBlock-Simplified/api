package dev.sbs.api.data.model.discord.command_data.command_groups;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.data.model.JsonModel;
import dev.sbs.api.util.builder.hash.EqualsBuilder;
import dev.sbs.api.util.builder.hash.HashCodeBuilder;
import lombok.Getter;
import org.hibernate.annotations.UpdateTimestamp;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Table;
import java.time.Instant;

@Table(
    name = "discord_command_groups"
)
public class CommandGroupJsonModel implements CommandGroupModel, JsonModel {

    @Getter
    private Long id;

    @Getter
    private String key;

    @Getter
    private String name;

    @Getter
    private String description;

    @Getter
    @UpdateTimestamp
    @SerializedName("updated_at")
    private @NotNull Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommandGroupJsonModel that = (CommandGroupJsonModel) o;

        return new EqualsBuilder()
            .append(this.getId(), that.getId())
            .append(this.getKey(), that.getKey())
            .append(this.getName(), that.getName())
            .append(this.getDescription(), that.getDescription())
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
            .append(this.getUpdatedAt())
            .build();
    }

}
