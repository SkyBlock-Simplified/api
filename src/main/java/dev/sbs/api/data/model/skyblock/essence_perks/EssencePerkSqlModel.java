package dev.sbs.api.data.model.skyblock.essence_perks;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.stats.StatSqlModel;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(
    name = "skyblock_essence_perks",
    indexes = {
        @Index(
            columnList = "key, stat_key",
            unique = true
        ),
        @Index(
            columnList = "stat_key"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EssencePerkSqlModel implements EssencePerkModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Getter
    @Setter
    @Id
    @Column(name = "key", nullable = false, length = 256)
    private String key;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "stat_key", nullable = false)
    private StatSqlModel stat;

    @Getter
    @Setter
    @Column(name = "level_bonus", nullable = false)
    private Integer levelBonus;

    @Getter
    @Setter
    @Column(name = "permanent", nullable = false)
    private boolean permanent;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EssencePerkSqlModel that = (EssencePerkSqlModel) o;

        return new EqualsBuilder()
            .append(this.isPermanent(), that.isPermanent())
            .append(this.getId(), that.getId())
            .append(this.getKey(), that.getKey())
            .append(this.getStat(), that.getStat())
            .append(this.getLevelBonus(), that.getLevelBonus())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getKey())
            .append(this.getStat())
            .append(this.getLevelBonus())
            .append(this.isPermanent())
            .append(this.getUpdatedAt())
            .build();
    }

}
