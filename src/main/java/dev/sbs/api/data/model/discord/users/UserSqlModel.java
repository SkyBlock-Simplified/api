package dev.sbs.api.data.model.discord.users;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.sql.converter.list.LongListConverter;
import dev.sbs.api.data.sql.converter.list.StringListConverter;
import dev.sbs.api.data.sql.converter.map.LongListStringMapConverter;
import dev.sbs.api.data.sql.converter.map.LongStringMapConverter;
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
import java.util.Map;

@Entity
@Table(
    name = "discord_users"
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserSqlModel implements UserModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Getter
    @Setter
    @Column(name = "discord_ids", nullable = false)
    @Convert(converter = LongListConverter.class)
    private List<Long> discordIds;

    @Getter
    @Setter
    @Column(name = "mojang_uuids", nullable = false)
    @Convert(converter = StringListConverter.class)
    private List<String> mojangUniqueIds;

    @Getter
    @Setter
    @Column(name = "notes", nullable = false)
    @Convert(converter = LongStringMapConverter.class)
    private Map<Long, String> notes;

    @Getter
    @Setter
    @Column(name = "guild_commands_blacklisted", nullable = false)
    @Convert(converter = LongListStringMapConverter.class)
    private Map<Long, List<String>> guildCommandsBlacklisted;

    @Getter
    @Setter
    @Column(name = "guild_reputation_blacklisted", nullable = false)
    @Convert(converter = LongListStringMapConverter.class)
    private Map<Long, List<String>> guildReputationBlacklisted;

    @Getter
    @Setter
    @Column(name = "guild_tickets_blacklisted", nullable = false)
    @Convert(converter = LongListStringMapConverter.class)
    private Map<Long, List<String>> guildTicketsBlacklisted;

    @Getter
    @Setter
    @Column(name = "developer_protected", nullable = false)
    private boolean developerProtected;

    @Getter
    @Setter
    @Column(name = "developer_reports_enabled", nullable = false)
    private boolean developerReportsEnabled;

    @Getter
    @Setter
    @Column(name = "developer_reputation_enabled", nullable = false)
    private boolean developerReputationEnabled;

    @Getter
    @Setter
    @Column(name = "developer_commands_enabled", nullable = false)
    private boolean developerCommandsEnabled;

    @Getter
    @Setter
    @Column(name = "developer_commands_blacklisted", nullable = false)
    @Convert(converter = StringListConverter.class)
    private List<String> developerCommandsBlacklisted;

    @Getter
    @Column(name = "submitted_at", nullable = false)
    private Instant submittedAt;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
    
}
