package dev.sbs.api.data.model.discord.guild_data.guilds;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.sql.converter.list.LongListConverter;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
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
    @Column(name = "developer_bot_enabled", nullable = false)
    private boolean developerBotEnabled;

    @Getter
    @Setter
    @Column(name = "reports_public", nullable = false)
    private boolean reportsPublic;

    @Getter
    @Setter
    @Column(name = "emoji_management", nullable = false)
    private boolean emojiServer;

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

        GuildSqlModel that = (GuildSqlModel) o;

        return new EqualsBuilder()
            .append(this.isDeveloperBotEnabled(), that.isDeveloperBotEnabled())
            .append(this.isReportsPublic(), that.isReportsPublic())
            .append(this.isEmojiServer(), that.isEmojiServer())
            .append(this.getId(), that.getId())
            .append(this.getGuildId(), that.getGuildId())
            .append(this.getName(), that.getName())
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
            .append(this.isDeveloperBotEnabled())
            .append(this.isReportsPublic())
            .append(this.isEmojiServer())
            .append(this.getAdminRoles())
            .append(this.getManagerRoles())
            .append(this.getModRoles())
            .append(this.getHelperRoles())
            .append(this.getSubmittedAt())
            .append(this.getUpdatedAt())
            .build();
    }

}
