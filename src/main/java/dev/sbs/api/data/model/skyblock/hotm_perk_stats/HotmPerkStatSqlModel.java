package dev.sbs.api.data.model.skyblock.hotm_perk_stats;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.hotm_perks.HotmPerkSqlModel;
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
    name = "skyblock_hotm_perk_stats",
    indexes = {
        @Index(
            columnList = "perk_key, stat_key",
            unique = true
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class HotmPerkStatSqlModel implements HotmPerkStatModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "perk_key", nullable = false)
    private HotmPerkSqlModel perk;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "stat_key", nullable = false)
    private StatSqlModel stat;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HotmPerkStatSqlModel)) return false;
        HotmPerkStatSqlModel that = (HotmPerkStatSqlModel) o;

        return new EqualsBuilder().append(this.getId(), that.getId()).append(this.getPerk(), that.getPerk()).append(this.getStat(), that.getStat()).append(this.getUpdatedAt(), that.getUpdatedAt()).build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.getId()).append(this.getPerk()).append(this.getStat()).append(this.getUpdatedAt()).build();
    }

}