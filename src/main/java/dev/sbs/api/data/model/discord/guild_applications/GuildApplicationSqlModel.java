package dev.sbs.api.data.model.discord.guild_applications;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.discord.guild_application_types.GuildApplicationTypeSqlModel;
import dev.sbs.api.data.model.discord.guild_embeds.GuildEmbedSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(
    name = "discord_guild_applications",
    indexes = {
        @Index(
            columnList = "guild_id, key",
            unique = true
        ),
        @Index(
            columnList = "guild_id, application_type_key"
        ),
        @Index(
            columnList = "guild_id, embed_key"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GuildApplicationSqlModel implements GuildApplicationModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Getter
    @Setter
    @Column(name = "key", nullable = false)
    private String key;

    @Getter
    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Getter
    @Setter
    @Column(name = "guild_id", nullable = false)
    private Long guildId;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "guild_id", nullable = false, referencedColumnName = "guild_id", insertable = false, updatable = false),
        @JoinColumn(name = "application_type_key", nullable = false, referencedColumnName = "key", insertable = false, updatable = false)
    })
    private GuildApplicationTypeSqlModel type;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "guild_id", nullable = false, referencedColumnName = "guild_id", insertable = false, updatable = false),
        @JoinColumn(name = "embed_key", nullable = false, referencedColumnName = "key", insertable = false, updatable = false)
    })
    private GuildEmbedSqlModel embed;

    @Getter
    @Setter
    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Getter
    @Setter
    @Column(name = "notes")
    private String notes;

    @Getter
    @Setter
    @Column(name = "live_at")
    private Instant liveAt;

    @Getter
    @Setter
    @Column(name = "close_at")
    private Instant closeAt;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GuildApplicationSqlModel that = (GuildApplicationSqlModel) o;

        return new EqualsBuilder()
            .append(this.isEnabled(), that.isEnabled())
            .append(this.getId(), that.getId())
            .append(this.getKey(), that.getKey())
            .append(this.getName(), that.getName())
            .append(this.getGuildId(), that.getGuildId())
            .append(this.getType(), that.getType())
            .append(this.getEmbed(), that.getEmbed())
            .append(this.getNotes(), that.getNotes())
            .append(this.getLiveAt(), that.getLiveAt())
            .append(this.getCloseAt(), that.getCloseAt())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getKey())
            .append(this.getName())
            .append(this.getGuildId())
            .append(this.getType())
            .append(this.getEmbed())
            .append(this.isEnabled())
            .append(this.getNotes())
            .append(this.getLiveAt())
            .append(this.getCloseAt())
            .append(this.getUpdatedAt())
            .build();
    }

}
