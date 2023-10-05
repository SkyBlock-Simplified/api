package dev.sbs.api.data.model.skyblock.reforge_data.reforge_stats;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.rarities.RaritySqlModel;
import dev.sbs.api.data.model.skyblock.reforge_data.reforges.ReforgeSqlModel;
import dev.sbs.api.data.sql.converter.map.StringDoubleMapConverter;
import dev.sbs.api.util.builder.hash.EqualsBuilder;
import dev.sbs.api.util.builder.hash.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(
    name = "skyblock_reforge_stats",
    indexes = {
        @Index(
            columnList = "key, rarity_key",
            unique = true
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ReforgeStatSqlModel implements ReforgeStatModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "key", nullable = false)
    private ReforgeSqlModel reforge;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "rarity_key")
    private RaritySqlModel rarity;

    @Getter
    @Setter
    @Column(name = "effects")
    @Convert(converter = StringDoubleMapConverter.class)
    private Map<String, Double> effects;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReforgeStatSqlModel that = (ReforgeStatSqlModel) o;

        return new EqualsBuilder()
            .append(this.getId(), that.getId())
            .append(this.getReforge(), that.getReforge())
            .append(this.getRarity(), that.getRarity())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getReforge())
            .append(this.getRarity())
            .append(this.getUpdatedAt())
            .build();
    }

}
