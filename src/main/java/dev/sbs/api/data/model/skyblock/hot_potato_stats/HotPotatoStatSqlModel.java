package dev.sbs.api.data.model.skyblock.hot_potato_stats;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.reforge_types.ReforgeTypeSqlModel;
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
    name = "skyblock_hot_potato_stats",
    indexes = {
        @Index(
            columnList = "reforge_type_key, stat_key",
            unique = true
        ),
        @Index(
            columnList = "reforge_type_key"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class HotPotatoStatSqlModel implements HotPotatoStatModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "reforge_type_key", nullable = false)
    private ReforgeTypeSqlModel type;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "stat_key", nullable = false)
    private StatSqlModel stat;

    @Getter
    @Setter
    @Column(name = "value", nullable = false)
    private Integer value;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HotPotatoStatSqlModel)) return false;
        HotPotatoStatSqlModel that = (HotPotatoStatSqlModel) o;

        return new EqualsBuilder().append(this.getId(), that.getId())
            .append(this.getType(), that.getType())
            .append(this.getStat(), that.getStat())
            .append(this.getValue(), that.getValue())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.getId()).append(this.getType()).append(this.getStat()).append(this.getValue()).append(this.getUpdatedAt()).build();
    }

}
