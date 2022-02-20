package dev.sbs.api.data.model.discord.users;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.sql.converter.list.LongListConverter;
import dev.sbs.api.data.sql.converter.list.UUIDListConverter;
import dev.sbs.api.data.sql.converter.map.LongStringMapConverter;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
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
import java.util.Map;
import java.util.UUID;

@Entity
@Table(
    name = "discord_users"
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserSqlModel implements UserModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Getter
    @Setter
    @Column(name = "discord_ids", nullable = false)
    @Convert(converter = LongListConverter.class)
    private List<Long> discordIds;

    @Getter
    @Setter
    @Column(name = "mojang_uuids", nullable = false)
    @Convert(converter = UUIDListConverter.class)
    private List<UUID> mojangUniqueIds;

    @Getter
    @Setter
    @Column(name = "notes", nullable = false)
    @Convert(converter = LongStringMapConverter.class)
    private Map<Long, String> notes;

    @Getter
    @Setter
    @Column(name = "guild_interaction_blacklisted", nullable = false)
    @Convert(converter = LongListConverter.class)
    private List<Long> guildInteractionBlacklisted;

    @Getter
    @Setter
    @Column(name = "developer_protected", nullable = false)
    private boolean developerProtected;

    @Getter
    @Setter
    @Column(name = "developer_interaction_enabled", nullable = false)
    private boolean botInteractionEnabled;

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

        UserSqlModel that = (UserSqlModel) o;

        return new EqualsBuilder()
            .append(this.isDeveloperProtected(), that.isDeveloperProtected())
            .append(this.isBotInteractionEnabled(), that.isBotInteractionEnabled())
            .append(this.getId(), that.getId())
            .append(this.getDiscordIds(), that.getDiscordIds())
            .append(this.getMojangUniqueIds(), that.getMojangUniqueIds())
            .append(this.getNotes(), that.getNotes())
            .append(this.getGuildInteractionBlacklisted(), that.getGuildInteractionBlacklisted())
            .append(this.getSubmittedAt(), that.getSubmittedAt())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getDiscordIds())
            .append(this.getMojangUniqueIds())
            .append(this.getNotes())
            .append(this.getGuildInteractionBlacklisted())
            .append(this.isDeveloperProtected())
            .append(this.isBotInteractionEnabled())
            .append(this.getSubmittedAt())
            .append(this.getUpdatedAt())
            .build();
    }

}
