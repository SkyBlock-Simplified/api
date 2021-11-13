package dev.sbs.api.data.model.reforge_stats;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.rarities.RaritySqlModel;
import dev.sbs.api.data.model.reforges.ReforgeSqlModel;
import dev.sbs.api.data.sql.converter.DoubleMapConverter;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(
        name = "reforge_stats",
        indexes = {
                @Index(
                        columnList = "key, rarity_key",
                        unique = true
                )
        }
)
public class ReforgeStatSqlModel implements ReforgeStatModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key", nullable = false)
    private ReforgeSqlModel reforge;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rarity_key")
    private RaritySqlModel rarity;

    @Getter
    @Setter
    @Column(name = "effects")
    @Convert(converter = DoubleMapConverter.class)
    private Map<String, Double> effects;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReforgeStatSqlModel)) return false;
        ReforgeStatSqlModel that = (ReforgeStatSqlModel) o;

        return new EqualsBuilder().append(this.getId(), that.getId()).append(this.getReforge(), that.getReforge()).append(this.getRarity(), that.getRarity()).append(this.getEffects(), that.getEffects()).append(this.getUpdatedAt(), that.getUpdatedAt()).build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.getId()).append(this.getReforge()).append(this.getRarity()).append(this.getEffects()).append(this.getUpdatedAt()).build();
    }

}
