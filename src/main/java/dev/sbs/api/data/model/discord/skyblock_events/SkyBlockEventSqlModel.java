package dev.sbs.api.data.model.discord.skyblock_events;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.discord.emojis.EmojiSqlModel;
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

@Entity
@Table(
    name = "discord_skyblock_events",
    indexes = {
        @Index(
            columnList = "emoji_key"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SkyBlockEventSqlModel implements SkyBlockEventModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Getter
    @Setter
    @Id
    @Column(name = "key", nullable = false)
    private String key;

    @Getter
    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "emoji_key")
    private EmojiSqlModel botEmoji;

    @Getter
    @Setter
    @Column(name = "description", nullable = false)
    private String description;

    @Getter
    @Setter
    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Getter
    @Setter
    @Column(name = "status")
    private String status;

    @Getter
    @Setter
    @Column(name = "interval_expression")
    private String intervalExpression;

    @Getter
    @Setter
    @Column(name = "thirdparty_json_url")
    private String thirdPartyJsonUrl;

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
        if (!(o instanceof SkyBlockEventSqlModel)) return false;
        SkyBlockEventSqlModel that = (SkyBlockEventSqlModel) o;

        return new EqualsBuilder().append(this.isEnabled(), that.isEnabled())
            .append(this.getId(), that.getId())
            .append(this.getKey(), that.getKey())
            .append(this.getName(), that.getName())
            .append(this.getBotEmoji(), that.getBotEmoji())
            .append(this.getDescription(), that.getDescription())
            .append(this.getStatus(), that.getStatus())
            .append(this.getIntervalExpression(), that.getIntervalExpression())
            .append(this.getThirdPartyJsonUrl(), that.getThirdPartyJsonUrl())
            .append(this.getSubmittedAt(), that.getSubmittedAt())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.getId())
            .append(this.getKey())
            .append(this.getName())
            .append(this.getBotEmoji())
            .append(this.getDescription())
            .append(this.isEnabled())
            .append(this.getStatus())
            .append(this.getIntervalExpression())
            .append(this.getThirdPartyJsonUrl())
            .append(this.getSubmittedAt())
            .append(this.getUpdatedAt())
            .build();
    }

}
