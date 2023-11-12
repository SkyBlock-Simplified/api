package dev.sbs.api.data.model.discord.command_data.commands;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.discord.command_data.command_categories.CommandCategorySqlModel;
import dev.sbs.api.data.model.discord.command_data.command_groups.CommandGroupSqlModel;
import dev.sbs.api.data.model.discord.command_data.command_parents.CommandParentSqlModel;
import dev.sbs.api.data.model.discord.emojis.EmojiSqlModel;
import dev.sbs.api.data.model.discord.guild_data.guilds.GuildSqlModel;
import dev.sbs.api.util.builder.hash.EqualsBuilder;
import dev.sbs.api.util.builder.hash.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
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
import java.util.UUID;

@Entity
@Table(
    name = "discord_commands",
    indexes = {
        @Index(
            columnList = "name, group_key, parent_key",
            unique = true
        ),
        @Index(
            columnList = "group_key"
        ),
        @Index(
            columnList = "parent_key"
        ),
        @Index(
            columnList = "emoji_key"
        ),
        @Index(
            columnList = "category_key"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CommandSqlModel implements CommandModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Getter
    @Setter
    @Id
    @Type(type = "uuid-char")
    @Column(name = "identifier", nullable = false)
    private UUID uniqueId;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "parent_key", referencedColumnName = "key")
    private CommandParentSqlModel parent;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "group_key", referencedColumnName = "key")
    private CommandGroupSqlModel group;

    @Getter
    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "guild_id", referencedColumnName = "guild_id")
    private GuildSqlModel guild;

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
    @Column(name = "developer_only", nullable = false)
    private boolean developerOnly;

    @Getter
    @Setter
    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Getter
    @Setter
    @Column(name = "status")
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

        CommandSqlModel that = (CommandSqlModel) o;

        return new EqualsBuilder()
            .append(this.isDeveloperOnly(), that.isDeveloperOnly())
            .append(this.isEnabled(), that.isEnabled())
            .append(this.getId(), that.getId())
            .append(this.getUniqueId(), that.getUniqueId())
            .append(this.getParent(), that.getParent())
            .append(this.getGroup(), that.getGroup())
            .append(this.getName(), that.getName())
            .append(this.getDescription(), that.getDescription())
            .append(this.getLongDescription(), that.getLongDescription())
            .append(this.getEmoji(), that.getEmoji())
            .append(this.getCategory(), that.getCategory())
            .append(this.getStatus(), that.getStatus())
            .append(this.getSubmittedAt(), that.getSubmittedAt())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getUniqueId())
            .append(this.getParent())
            .append(this.getGroup())
            .append(this.getName())
            .append(this.getDescription())
            .append(this.getLongDescription())
            .append(this.getEmoji())
            .append(this.getCategory())
            .append(this.isDeveloperOnly())
            .append(this.isEnabled())
            .append(this.getStatus())
            .append(this.getSubmittedAt())
            .append(this.getUpdatedAt())
            .build();
    }

}
