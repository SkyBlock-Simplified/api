package dev.sbs.api.data.model.discord.guild_data.application_data.guild_application_requirements;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.discord.application_requirements.ApplicationRequirementSqlModel;
import dev.sbs.api.data.model.discord.guild_data.application_data.guild_applications.GuildApplicationSqlModel;
import dev.sbs.api.data.model.discord.guild_data.guilds.GuildSqlModel;
import dev.sbs.api.data.model.discord.setting_types.SettingTypeSqlModel;
import dev.sbs.api.util.builder.hash.EqualsBuilder;
import dev.sbs.api.util.builder.hash.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;

@Getter
@Entity
@Table(
    name = "discord_guild_application_requirements",
    indexes = {
        @Index(
            columnList = "guild_id, application_key, requirement_key",
            unique = true
        ),
        @Index(
            columnList = "application_key"
        ),
        @Index(
            columnList = "requirement_key"
        ),
        @Index(
            columnList = "setting_type_key"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GuildApplicationRequirementSqlModel implements GuildApplicationRequirementModel, SqlModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "guild_id", nullable = false)
    private GuildSqlModel guild;

    @Setter
    @ManyToOne
    @JoinColumn(name = "application_key", nullable = false, referencedColumnName = "key", insertable = false, updatable = false)
    private GuildApplicationSqlModel application;

    @Setter
    @ManyToOne
    @JoinColumn(name = "requirement_key", nullable = false, referencedColumnName = "key", insertable = false, updatable = false)
    private ApplicationRequirementSqlModel requirement;

    @Setter
    @ManyToOne
    @JoinColumn(name = "setting_type_key", nullable = false, referencedColumnName = "key", insertable = false, updatable = false)
    private SettingTypeSqlModel type;

    @Setter
    @Column(name = "value", nullable = false)
    private String value;

    @Setter
    @Column(name = "description")
    private String description;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @CreationTimestamp
    @Column(name = "submitted_at", nullable = false)
    private Instant submittedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GuildApplicationRequirementSqlModel that = (GuildApplicationRequirementSqlModel) o;

        return new EqualsBuilder()
            .append(this.getId(), that.getId())
            .append(this.getGuild(), that.getGuild())
            .append(this.getApplication(), that.getApplication())
            .append(this.getRequirement(), that.getRequirement())
            .append(this.getType(), that.getType())
            .append(this.getValue(), that.getValue())
            .append(this.getDescription(), that.getDescription())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .append(this.getSubmittedAt(), that.getSubmittedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getGuild())
            .append(this.getApplication())
            .append(this.getRequirement())
            .append(this.getType())
            .append(this.getValue())
            .append(this.getDescription())
            .append(this.getUpdatedAt())
            .append(this.getSubmittedAt())
            .build();
    }

}