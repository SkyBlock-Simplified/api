package dev.sbs.api.data.model.discord.guilds;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.sql.converter.list.LongListConverter;
import dev.sbs.api.data.sql.converter.list.StringListConverter;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;
import java.util.List;

@Entity
@Table(
    name = "discord_guilds"
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GuildSqlModel implements GuildModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Getter
    @Setter
    @Id
    @Column(name = "guild_id", nullable = false)
    private Long guildId;

    @Getter
    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Getter
    @Setter
    @Column(name = "reports_visible", nullable = false)
    private boolean reportsVisible;

    @Getter
    @Setter
    @Column(name = "bot_enabled", nullable = false)
    private boolean botEnabled;

    @Getter
    @Setter
    @Column(name = "emoji_management", nullable = false)
    private boolean emojiServer;

    @Getter
    @Setter
    @Column(name = "developer_reports_enabled", nullable = false)
    private boolean developerReportsEnabled;

    @Getter
    @Setter
    @Column(name = "developer_tickets_enabled", nullable = false)
    private boolean developerTicketsEnabled;

    @Getter
    @Setter
    @Column(name = "developer_reputation_enabled", nullable = false)
    private boolean developerReputationEnabled;

    @Getter
    @Setter
    @Column(name = "developer_bot_enabled", nullable = false)
    private boolean developerBotEnabled;

    @Getter
    @Setter
    @Column(name = "developer_commands_blacklisted", nullable = false)
    @Convert(converter = StringListConverter.class)
    private List<String> developerBlacklistedCommands;

    @Getter
    @Setter
    @Column(name = "admin_roles", nullable = false)
    @Convert(converter = LongListConverter.class)
    private List<Long> adminRoles;

    @Getter
    @Setter
    @Column(name = "manager_roles", nullable = false)
    @Convert(converter = LongListConverter.class)
    private List<Long> managerRoles;

    @Getter
    @Setter
    @Column(name = "mod_roles", nullable = false)
    @Convert(converter = LongListConverter.class)
    private List<Long> modRoles;

    @Getter
    @Setter
    @Column(name = "helper_roles", nullable = false)
    @Convert(converter = LongListConverter.class)
    private List<Long> helperRoles;

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
        if (!(o instanceof GuildSqlModel)) return false;
        GuildSqlModel that = (GuildSqlModel) o;

        return new EqualsBuilder()
            .append(this.isReportsVisible(), that.isReportsVisible())
            .append(this.isBotEnabled(), that.isBotEnabled())
            .append(this.isEmojiServer(), that.isEmojiServer())
            .append(this.isDeveloperReportsEnabled(), that.isDeveloperReportsEnabled())
            .append(this.isDeveloperTicketsEnabled(), that.isDeveloperTicketsEnabled())
            .append(this.isDeveloperReputationEnabled(), that.isDeveloperReputationEnabled())
            .append(this.isDeveloperBotEnabled(), that.isDeveloperBotEnabled())
            .append(this.getId(), that.getId())
            .append(this.getGuildId(), that.getGuildId())
            .append(this.getName(), that.getName())
            .append(this.getDeveloperBlacklistedCommands(), that.getDeveloperBlacklistedCommands())
            .append(this.getAdminRoles(), that.getAdminRoles())
            .append(this.getManagerRoles(), that.getManagerRoles())
            .append(this.getModRoles(), that.getModRoles())
            .append(this.getHelperRoles(), that.getHelperRoles())
            .append(this.getSubmittedAt(), that.getSubmittedAt())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getGuildId())
            .append(this.getName())
            .append(this.isReportsVisible())
            .append(this.isBotEnabled())
            .append(this.isEmojiServer())
            .append(this.isDeveloperReportsEnabled())
            .append(this.isDeveloperTicketsEnabled())
            .append(this.isDeveloperReputationEnabled())
            .append(this.isDeveloperBotEnabled())
            .append(this.getDeveloperBlacklistedCommands())
            .append(this.getAdminRoles())
            .append(this.getManagerRoles())
            .append(this.getModRoles())
            .append(this.getHelperRoles())
            .append(this.getSubmittedAt())
            .append(this.getUpdatedAt())
            .build();
    }

}
