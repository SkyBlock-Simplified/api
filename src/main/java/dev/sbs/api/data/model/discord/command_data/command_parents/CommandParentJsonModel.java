package dev.sbs.api.data.model.discord.command_data.command_parents;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.data.model.JsonModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Table;
import java.time.Instant;

@Table(
    name = "discord_command_parents"
)
public class CommandParentJsonModel implements CommandParentModel, JsonModel {

    @Getter
    private Long id;

    @Getter
    @Setter
    private String key;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private boolean prefix;

    @Getter
    @UpdateTimestamp
    @SerializedName("updated_at")
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommandParentJsonModel that = (CommandParentJsonModel) o;

        return new EqualsBuilder()
            .append(this.getId(), that.getId())
            .append(this.getKey(), that.getKey())
            .append(this.getName(), that.getName())
            .append(this.getDescription(), that.getDescription())
            .append(this.isPrefix(), that.isPrefix())
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
            .append(this.isPrefix())
            .append(this.getUpdatedAt())
            .build();
    }

}
