package dev.sbs.api.data.model.discord.guild_data.guild_skyblock_events;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.discord.guild_data.guilds.GuildSqlModel;
import dev.sbs.api.data.model.discord.skyblock_events.SkyBlockEventSqlModel;
import dev.sbs.api.data.sql.converter.list.LongListConverter;
import dev.sbs.api.util.builder.hash.EqualsBuilder;
import dev.sbs.api.util.builder.hash.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(
    name = "discord_guild_skyblock_events",
    indexes = {
        @Index(
            columnList = "guild_id, event_key",
            unique = true
        ),
        @Index(
            columnList = "guild_id"
        ),
        @Index(
            columnList = "event_key"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GuildSkyBlockEventSqlModel implements GuildSkyBlockEventModel, SqlModel {

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
    @ManyToOne
    @JoinColumn(name = "event_key", nullable = false)
    private SkyBlockEventSqlModel event;

    @Getter
    @Setter
    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Getter
    @Setter
    @Column(name = "mention_roles", nullable = false)
    @Convert(converter = LongListConverter.class)
    private List<Long> mentionRoles;

    @Getter
    @Setter
    @Column(name = "webhook_url")
    private String webhookUrl;

    @Getter
    @Setter
    @Column(name = "channel_id")
    private Long channelId;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GuildSkyBlockEventSqlModel)) return false;
        GuildSkyBlockEventSqlModel that = (GuildSkyBlockEventSqlModel) o;

        return new EqualsBuilder().append(this.isEnabled(), that.isEnabled())
            .append(this.getId(), that.getId())
            .append(this.getGuild(), that.getGuild())
            .append(this.getEvent(), that.getEvent())
            .append(this.getMentionRoles(), that.getMentionRoles())
            .append(this.getWebhookUrl(), that.getWebhookUrl())
            .append(this.getChannelId(), that.getChannelId())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.getId())
            .append(this.getGuild())
            .append(this.getEvent())
            .append(this.isEnabled())
            .append(this.getMentionRoles())
            .append(this.getWebhookUrl())
            .append(this.getChannelId())
            .append(this.getUpdatedAt())
            .build();
    }

}
