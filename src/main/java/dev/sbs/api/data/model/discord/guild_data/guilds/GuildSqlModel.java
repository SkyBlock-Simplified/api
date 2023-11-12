package dev.sbs.api.data.model.discord.guild_data.guilds;

import dev.sbs.api.data.model.SqlModel;
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
import javax.persistence.Table;
import java.time.Instant;

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
    @Column(name = "reports_public", nullable = false)
    private boolean reportsPublic;

    @Getter
    @Setter
    @Column(name = "emoji_management", nullable = false)
    private boolean emojiServer;

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
            .append(this.isReportsPublic(), that.isReportsPublic())
            .append(this.isEmojiServer(), that.isEmojiServer())
            .append(this.getId(), that.getId())
            .append(this.getGuildId(), that.getGuildId())
            .append(this.getName(), that.getName())
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
            .append(this.isReportsPublic())
            .append(this.isEmojiServer())
            .append(this.getSubmittedAt())
            .append(this.getUpdatedAt())
            .build();
    }

}
