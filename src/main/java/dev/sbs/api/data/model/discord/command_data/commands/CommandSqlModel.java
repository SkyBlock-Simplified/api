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
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Table(
    name = "discord_commands",
    indexes = {
        @Index(
            columnList = "name, group_key, parent_key, guild_id",
            unique = true
        ),
        @Index(
            columnList = "emoji_key"
        ),
        @Index(
            columnList = "category_key"
        ),
        @Index(
            columnList = "group_key"
        ),
        @Index(
            columnList = "parent_key"
        ),
        @Index(
            columnList = "guild_id"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CommandSqlModel implements CommandModel, SqlModel {

    @Id
    @Setter
    @Type(type = "uuid-char")
    @Column(name = "identifier")
    private UUID uniqueId;

    @Setter
    @ManyToOne
    @JoinColumn(name = "parent_key", referencedColumnName = "key")
    private CommandParentSqlModel parent;

    @Setter
    @ManyToOne
    @JoinColumn(name = "group_key", referencedColumnName = "key")
    private CommandGroupSqlModel group;

    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Setter
    @ManyToOne
    @JoinColumn(name = "guild_id", referencedColumnName = "guild_id")
    private GuildSqlModel guild;

    @Setter
    @ManyToOne
    @JoinColumn(name = "emoji_key", referencedColumnName = "key")
    private EmojiSqlModel emoji;

    @Setter
    @ManyToOne
    @JoinColumn(name = "category_key", referencedColumnName = "key")
    private CommandCategorySqlModel category;

    @Setter
    @Column(name = "description", nullable = false)
    private String description;

    @Setter
    @Column(name = "long_description")
    private String longDescription;

    @Setter
    @Column(name = "default_member_permissions", nullable = false)
    private Long defaultMemberPermissions;

    @Setter
    @Column(name = "nsfw", nullable = false)
    private boolean nsfw;

    @Setter
    @Column(name = "dm_permission", nullable = false)
    private boolean privateChannelEnabled;

    @Setter
    @Column(name = "developer_only", nullable = false)
    private boolean developerOnly;

    @Setter
    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Setter
    @Column(name = "status")
    private String status;

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

        CommandSqlModel that = (CommandSqlModel) o;

        return new EqualsBuilder()
            .append(this.isNsfw(), that.isNsfw())
            .append(this.isPrivateChannelEnabled(), that.isPrivateChannelEnabled())
            .append(this.isDeveloperOnly(), that.isDeveloperOnly())
            .append(this.isEnabled(), that.isEnabled())
            .append(this.getUniqueId(), that.getUniqueId())
            .append(this.getParent(), that.getParent())
            .append(this.getGroup(), that.getGroup())
            .append(this.getName(), that.getName())
            .append(this.getGuild(), that.getGuild())
            .append(this.getEmoji(), that.getEmoji())
            .append(this.getCategory(), that.getCategory())
            .append(this.getDescription(), that.getDescription())
            .append(this.getLongDescription(), that.getLongDescription())
            .append(this.getDefaultMemberPermissions(), that.getDefaultMemberPermissions())
            .append(this.getStatus(), that.getStatus())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .append(this.getSubmittedAt(), that.getSubmittedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getUniqueId())
            .append(this.getParent())
            .append(this.getGroup())
            .append(this.getName())
            .append(this.getGuild())
            .append(this.getEmoji())
            .append(this.getCategory())
            .append(this.getDescription())
            .append(this.getLongDescription())
            .append(this.getDefaultMemberPermissions())
            .append(this.isNsfw())
            .append(this.isPrivateChannelEnabled())
            .append(this.isDeveloperOnly())
            .append(this.isEnabled())
            .append(this.getStatus())
            .append(this.getUpdatedAt())
            .append(this.getSubmittedAt())
            .build();
    }

}
