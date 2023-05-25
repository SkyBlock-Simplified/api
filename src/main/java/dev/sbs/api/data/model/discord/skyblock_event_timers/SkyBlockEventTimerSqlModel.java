package dev.sbs.api.data.model.discord.skyblock_event_timers;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.discord.skyblock_events.SkyBlockEventSqlModel;
import dev.sbs.api.data.model.skyblock.seasons.SeasonSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(
    name = "discord_skyblock_event_timers",
    indexes = {
        @Index(
            columnList = "event_key"
        ),
        @Index(
            columnList = "start_season_key"
        ),
        @Index(
            columnList = "end_season_key"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SkyBlockEventTimerSqlModel implements SkyBlockEventTimerModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Getter
    @Setter
    @Column(name = "event_key", nullable = false)
    private SkyBlockEventSqlModel event;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "start_season_key", nullable = false)
    private SeasonSqlModel start;

    @Getter
    @Setter
    @Column(name = "start_season_day", nullable = false)
    private Integer startDay;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "end_season_key", nullable = false)
    private SeasonSqlModel end;

    @Getter
    @Setter
    @Column(name = "end_season_day", nullable = false)
    private Integer endDay;

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
        if (!(o instanceof SkyBlockEventTimerSqlModel)) return false;
        SkyBlockEventTimerSqlModel that = (SkyBlockEventTimerSqlModel) o;

        return new EqualsBuilder().append(this.getId(), that.getId())
            .append(this.getEvent(), that.getEvent())
            .append(this.getStart(), that.getStart())
            .append(this.getStartDay(), that.getStartDay())
            .append(this.getEnd(), that.getEnd())
            .append(this.getEndDay(), that.getEndDay())
            .append(this.getSubmittedAt(), that.getSubmittedAt())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.getId())
            .append(this.getEvent())
            .append(this.getStart())
            .append(this.getStartDay())
            .append(this.getEnd())
            .append(this.getEndDay())
            .append(this.getSubmittedAt())
            .append(this.getUpdatedAt())
            .build();
    }

}
