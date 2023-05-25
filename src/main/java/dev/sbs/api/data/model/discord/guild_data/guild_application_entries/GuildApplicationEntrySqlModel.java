package dev.sbs.api.data.model.discord.guild_data.guild_application_entries;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.discord.guild_data.guild_applications.GuildApplicationSqlModel;
import dev.sbs.api.data.model.discord.guild_data.guilds.GuildSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(
    name = "discord_guild_application_entries",
    indexes = {
        @Index(
            columnList = "guild_id, application_key, submitter_discord_id",
            unique = true
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GuildApplicationEntrySqlModel implements GuildApplicationEntryModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "guild_id", nullable = false)
    private GuildSqlModel guild;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "application_key", nullable = false, referencedColumnName = "key")
    private GuildApplicationSqlModel application;

    @Getter
    @Setter
    @Column(name = "submitter_discord_id", nullable = false)
    private Long submitterDiscordId;

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

        GuildApplicationEntrySqlModel that = (GuildApplicationEntrySqlModel) o;

        return new EqualsBuilder()
            .append(this.getId(), that.getId())
            .append(this.getGuild(), that.getGuild())
            .append(this.getApplication(), that.getApplication())
            .append(this.getSubmitterDiscordId(), that.getSubmitterDiscordId())
            .append(this.getSubmittedAt(), that.getSubmittedAt())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getGuild())
            .append(this.getApplication())
            .append(this.getSubmitterDiscordId())
            .append(this.getSubmittedAt())
            .append(this.getUpdatedAt())
            .build();
    }

}
