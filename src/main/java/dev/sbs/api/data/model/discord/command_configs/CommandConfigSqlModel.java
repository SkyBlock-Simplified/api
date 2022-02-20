package dev.sbs.api.data.model.discord.command_configs;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.discord.command_categories.CommandCategorySqlModel;
import dev.sbs.api.data.model.discord.command_groups.CommandGroupSqlModel;
import dev.sbs.api.data.model.discord.emojis.EmojiSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;

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
    @Column(name = "description", nullable = false)
    private String description;

    @Getter
    @Setter
    @Column(name = "long_description")
    private String longDescription;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "emoji_key", referencedColumnName = "key")
    private EmojiSqlModel emoji;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "category_key", referencedColumnName = "key")
    private CommandCategorySqlModel category;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "group_key", referencedColumnName = "key")
    private CommandGroupSqlModel group;

    @Getter
    @Setter
    @Column(name = "developer_only", nullable = false)
    private boolean developerOnly;

    @Getter
    @Setter
    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Getter
    @Setter
    @Column(name = "guild_toggleable", nullable = false)
    private boolean guildToggleable;

    @Getter
    @Setter
    @Column(name = "status", nullable = false)
    private String status;

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

        CommandConfigSqlModel that = (CommandConfigSqlModel) o;

        return new EqualsBuilder()
            .append(this.isDeveloperOnly(), that.isDeveloperOnly())
            .append(this.isEnabled(), that.isEnabled())
            .append(this.isGuildToggleable(), that.isGuildToggleable())
            .append(this.getId(), that.getId())
            .append(this.getName(), that.getName())
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
            .append(this.isDeveloperOnly())
            .append(this.isEnabled())
            .append(this.isGuildToggleable())
            .append(this.getStatus())
            .append(this.getSubmittedAt())
            .append(this.getUpdatedAt())
            .build();
    }

}
