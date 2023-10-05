package dev.sbs.api.data.model.skyblock.hot_potato_stats;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.stats.StatSqlModel;
import dev.sbs.api.data.sql.converter.list.StringListConverter;
import dev.sbs.api.util.builder.hash.EqualsBuilder;
import dev.sbs.api.util.builder.hash.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(
    name = "skyblock_hot_potato_stats",
    indexes = {
        @Index(
            columnList = "group_key, stat_key",
            unique = true
        ),
        @Index(
            columnList = "group_key"
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
    @Column(name = "group_key", nullable = false)
    private String groupKey;

    @Getter
    @Setter
    @Column(name = "item_types", nullable = false)
    @Convert(converter = StringListConverter.class)
    private List<String> itemTypes;

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
        if (o == null || getClass() != o.getClass()) return false;

        HotPotatoStatSqlModel that = (HotPotatoStatSqlModel) o;

        return new EqualsBuilder()
            .append(this.getId(), that.getId())
            .append(this.getGroupKey(), that.getGroupKey())
            .append(this.getItemTypes(), that.getItemTypes())
            .append(this.getStat(), that.getStat())
            .append(this.getValue(), that.getValue())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getGroupKey())
            .append(this.getItemTypes())
            .append(this.getStat())
            .append(this.getValue())
            .append(this.getUpdatedAt())
            .build();
    }

}
