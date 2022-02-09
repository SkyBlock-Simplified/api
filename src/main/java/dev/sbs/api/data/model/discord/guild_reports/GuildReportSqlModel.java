package dev.sbs.api.data.model.discord.guild_reports;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.discord.guild_report_types.GuildReportTypeSqlModel;
import dev.sbs.api.data.sql.converter.list.StringListConverter;
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
    name = "discord_guild_reports",
    indexes = {
        @Index(
            columnList = "guild_id, report_type_key"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GuildReportSqlModel implements GuildReportModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "guild_id", nullable = false, referencedColumnName = "guild_id"),
        @JoinColumn(name = "report_type_key", nullable = false, referencedColumnName = "key")
    })
    private GuildReportTypeSqlModel type;

    @Getter
    @Setter
    @Column(name = "reported_discord_id")
    private Long reportedDiscordId;

    @Getter
    @Setter
    @Column(name = "reported_mojang_uuid")
    private String reportedMojangUniqueId;

    @Getter
    @Setter
    @Column(name = "submitter_discord_id", nullable = false)
    private Long submitterDiscordId;

    @Getter
    @Setter
    @Column(name = "assignee_discord_id")
    private Long assigneeDiscordId;

    @Getter
    @Setter
    @Column(name = "reason")
    private String reason;

    @Getter
    @Setter
    @Column(name = "proof")
    private String proof;

    @Getter
    @Setter
    @Column(name = "media_links", nullable = false)
    @Convert(converter = StringListConverter.class)
    private List<String> mediaLinks;

    @Getter
    @Setter
    @Column(name = "notes")
    private String notes;

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
        if (!(o instanceof GuildReportSqlModel)) return false;
        GuildReportSqlModel that = (GuildReportSqlModel) o;

        return new EqualsBuilder().append(this.getId(), that.getId())
            .append(this.getType(), that.getType())
            .append(this.getReportedDiscordId(), that.getReportedDiscordId())
            .append(this.getReportedMojangUniqueId(), that.getReportedMojangUniqueId())
            .append(this.getSubmitterDiscordId(), that.getSubmitterDiscordId())
            .append(this.getAssigneeDiscordId(), that.getAssigneeDiscordId())
            .append(this.getReason(), that.getReason())
            .append(this.getProof(), that.getProof())
            .append(this.getMediaLinks(), that.getMediaLinks())
            .append(this.getNotes(), that.getNotes())
            .append(this.getSubmittedAt(), that.getSubmittedAt())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.getId())
            .append(this.getType())
            .append(this.getReportedDiscordId())
            .append(this.getReportedMojangUniqueId())
            .append(this.getSubmitterDiscordId())
            .append(this.getAssigneeDiscordId())
            .append(this.getReason())
            .append(this.getProof())
            .append(this.getMediaLinks())
            .append(this.getNotes())
            .append(this.getSubmittedAt())
            .append(this.getUpdatedAt())
            .build();
    }

}
