package dev.sbs.api.data.model.discord.sbs_beta_testers;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(
    name = "discord_sbs_beta_testers",
    indexes = {
        @Index(
            columnList = "discord_id, early",
            unique = true
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SbsBetaTesterSqlModel implements SbsBetaTesterModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Getter
    @Setter
    @Column(name = "discord_id", nullable = false)
    private Long discordId;

    @Getter
    @Setter
    @Column(name = "early", nullable = false)
    private boolean early;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SbsBetaTesterSqlModel that = (SbsBetaTesterSqlModel) o;

        return new EqualsBuilder()
            .append(this.isEarly(), that.isEarly())
            .append(this.getId(), that.getId())
            .append(this.getDiscordId(), that.getDiscordId())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getDiscordId())
            .append(this.isEarly())
            .append(this.getUpdatedAt())
            .build();
    }

}
