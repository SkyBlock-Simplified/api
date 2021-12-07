package dev.sbs.api.data.model.discord.guild_application_requirements;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.discord.application_requirements.ApplicationRequirementSqlModel;
import dev.sbs.api.data.model.discord.guild_applications.GuildApplicationSqlModel;
import dev.sbs.api.data.model.discord.setting_evals.SettingEvalSqlModel;
import dev.sbs.api.data.model.discord.setting_types.SettingTypeSqlModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(
    name = "discord_guild_application_requirements",
    indexes = {
        @Index(
            columnList = "guild_id, application_key"
        ),
        @Index(
            columnList = "requirement_key"
        ),
        @Index(
            columnList = "setting_eval_key"
        ),
        @Index(
            columnList = "setting_type_key"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GuildApplicationRequirementSqlModel implements GuildApplicationRequirementModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "guild_id", nullable = false, referencedColumnName = "guild_id"),
        @JoinColumn(name = "application_key", nullable = false, referencedColumnName = "key")
    })
    private GuildApplicationSqlModel application;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "requirement_key", nullable = false, referencedColumnName = "key")
    private ApplicationRequirementSqlModel requirement;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "setting_eval_key", nullable = false, referencedColumnName = "key")
    private SettingEvalSqlModel eval;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "setting_type_key", nullable = false, referencedColumnName = "key")
    private SettingTypeSqlModel type;

    @Getter
    @Setter
    @Column(name = "value", nullable = false)
    private String value;

    @Getter
    @Setter
    @Column(name = "description")
    private String description;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
    
}
