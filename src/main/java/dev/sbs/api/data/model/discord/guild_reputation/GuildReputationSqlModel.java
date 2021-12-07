package dev.sbs.api.data.model.discord.guild_reputation;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.discord.guild_reputation_types.GuildReputationTypeSqlModel;
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
    name = "discord_guild_reputation",
    indexes = {
        @Index(
            columnList = "guild_id, reputation_type_key"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GuildReputationSqlModel implements GuildReputationModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "guild_id", nullable = false, referencedColumnName = "guild_id"),
        @JoinColumn(name = "reputation_type_key", nullable = false, referencedColumnName = "key")
    })
    private GuildReputationTypeSqlModel reputationType;

    @Getter
    @Setter
    @JoinColumn(name = "reported_discord_id", nullable = false)
    private Long reportedDiscordId;

    @Getter
    @Setter
    @JoinColumn(name = "submitter_discord_id", nullable = false)
    private Long submitterDiscordId;

    @Getter
    @Setter
    @JoinColumn(name = "assignee_discord_id", nullable = false)
    private Long assigneeDiscordId;

    @Getter
    @Setter
    @Column(name = "reason")
    private String reason;

    @Getter
    @Setter
    @Column(name = "positive")
    private boolean positive;

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
        if (!(o instanceof GuildReputationSqlModel)) return false;
        GuildReputationSqlModel that = (GuildReputationSqlModel) o;

        return new EqualsBuilder().append(this.isPositive(), that.isPositive())
            .append(this.getId(), that.getId())
            .append(this.getReputationType(), that.getReputationType())
            .append(this.getReportedDiscordId(), that.getReportedDiscordId())
            .append(this.getSubmitterDiscordId(), that.getSubmitterDiscordId())
            .append(this.getAssigneeDiscordId(), that.getAssigneeDiscordId())
            .append(this.getReason(), that.getReason())
            .append(this.getSubmittedAt(), that.getSubmittedAt())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.getId())
            .append(this.getReputationType())
            .append(this.getReportedDiscordId())
            .append(this.getSubmitterDiscordId())
            .append(this.getAssigneeDiscordId())
            .append(this.getReason())
            .append(this.isPositive())
            .append(this.getSubmittedAt())
            .append(this.getUpdatedAt())
            .build();
    }

}
