package dev.sbs.api.data.model.discord.guild_data.guild_command_configs;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.discord.guild_data.guilds.GuildSqlModel;
import dev.sbs.api.data.sql.converter.list.LongListConverter;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(
    name = "discord_guild_command_configs",
    indexes = {
        @Index(
            columnList = "guild_id, name",
            unique = true
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GuildCommandConfigSqlModel implements GuildCommandConfigModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "guild_id", nullable = false)
    private GuildSqlModel guild;

    @Getter
    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Getter
    @Setter
    @Column(name = "permission_override", nullable = false)
    private String permissionOverride;

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
    @CreationTimestamp
    @Column(name = "submitted_at", nullable = false)
    private Instant submittedAt;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GuildCommandConfigSqlModel that = (GuildCommandConfigSqlModel) o;

        return new EqualsBuilder()
            .append(this.getId(), that.getId())
            .append(this.getGuild(), that.getGuild())
            .append(this.getName(), that.getName())
            .append(this.getPermissionOverride(), that.getPermissionOverride())
            .append(this.getUsers(), that.getUsers())
            .append(this.getRoles(), that.getRoles())
            .append(this.getSubmittedAt(), that.getSubmittedAt())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getGuild())
            .append(this.getName())
            .append(this.getPermissionOverride())
            .append(this.getUsers())
            .append(this.getRoles())
            .append(this.getSubmittedAt())
            .append(this.getUpdatedAt())
            .build();
    }

}