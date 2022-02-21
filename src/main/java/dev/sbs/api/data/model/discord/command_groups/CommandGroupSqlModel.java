package dev.sbs.api.data.model.discord.command_groups;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(
    name = "discord_command_groups"
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CommandGroupSqlModel implements CommandGroupModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Getter
    @Setter
    @Column(name = "key", nullable = false, unique = true)
    private String key;

    @Getter
    @Setter
    @Column(name = "group", nullable = false, unique = true)
    private String group;

    @Getter
    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Getter
    @Setter
    @Column(name = "description", nullable = false)
    private String description;

    @Getter
    @Setter
    @Column(name = "required")
    private boolean required;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommandGroupSqlModel that = (CommandGroupSqlModel) o;

        return new EqualsBuilder()
            .append(this.isRequired(), that.isRequired())
            .append(this.getId(), that.getId())
            .append(this.getKey(), that.getKey())
            .append(this.getGroup(), that.getGroup())
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
            .append(this.getGroup())
            .append(this.getName())
            .append(this.getDescription())
            .append(this.isRequired())
            .append(this.getUpdatedAt())
            .build();
    }

}
