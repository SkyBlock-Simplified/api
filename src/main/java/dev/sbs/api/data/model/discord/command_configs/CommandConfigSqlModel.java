package dev.sbs.api.data.model.discord.command_configs;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.sql.converter.list.LongListConverter;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;
import java.util.List;

@Entity
@Table(
    name = "discord_command_configs"
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CommandConfigSqlModel implements CommandConfigModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Getter
    @Setter
    @Id
    @Column(name = "name", nullable = false)
    private String name;

    @Getter
    @Setter
    @Column(name = "bot_owner", nullable = false)
    private boolean botOwnerOnly;

    @Getter
    @Setter
    @Column(name = "user_list", nullable = false)
    @Convert(converter = LongListConverter.class)
    private List<Long> users;

    @Getter
    @Setter
    @Column(name = "role_list", nullable = false)
    @Convert(converter = LongListConverter.class)
    private List<Long> roles;

    @Getter
    @Setter
    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Getter
    @Setter
    @Column(name = "status", nullable = false)
    private String status;

    @Getter
    @Column(name = "submitted_at", nullable = false)
    private Instant submittedAt;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommandConfigSqlModel)) return false;
        CommandConfigSqlModel that = (CommandConfigSqlModel) o;

        return new EqualsBuilder()
            .append(this.isBotOwnerOnly(), that.isBotOwnerOnly())
            .append(this.isEnabled(), that.isEnabled())
            .append(this.getId(), that.getId())
            .append(this.getName(), that.getName())
            .append(this.getUsers(), that.getUsers())
            .append(this.getRoles(), that.getRoles())
            .append(this.getStatus(), that.getStatus())
            .append(this.getSubmittedAt(), that.getSubmittedAt())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getName())
            .append(this.isBotOwnerOnly())
            .append(this.getUsers())
            .append(this.getRoles())
            .append(this.isEnabled())
            .append(this.getStatus())
            .append(this.getSubmittedAt())
            .append(this.getUpdatedAt())
            .build();
    }

}
