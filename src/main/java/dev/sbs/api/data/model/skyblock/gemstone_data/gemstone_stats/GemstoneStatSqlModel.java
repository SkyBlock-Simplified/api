package dev.sbs.api.data.model.skyblock.gemstone_data.gemstone_stats;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.gemstone_data.gemstone_types.GemstoneTypeSqlModel;
import dev.sbs.api.data.model.skyblock.gemstone_data.gemstones.GemstoneSqlModel;
import dev.sbs.api.data.model.skyblock.rarities.RaritySqlModel;
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
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(
    name = "skyblock_gemstone_stats",
    indexes = {
        @Index(
            columnList = "gemstone_key, type_key, rarity_key",
            unique = true
        ),
        @Index(
            columnList = "type_key"
        ),
        @Index(
            columnList = "rarity_key"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GemstoneStatSqlModel implements GemstoneStatModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "gemstone_key", nullable = false)
    private GemstoneSqlModel gemstone;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "type_key", nullable = false)
    private GemstoneTypeSqlModel type;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "rarity_key", nullable = false)
    private RaritySqlModel rarity;

    @Getter
    @Setter
    @Column(name = "value", nullable = false)
    private Double value;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GemstoneStatSqlModel that = (GemstoneStatSqlModel) o;

        return new EqualsBuilder()
            .append(this.getId(), that.getId())
            .append(this.getGemstone(), that.getGemstone())
            .append(this.getType(), that.getType())
            .append(this.getRarity(), that.getRarity())
            .append(this.getValue(), that.getValue())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getGemstone())
            .append(this.getType())
            .append(this.getRarity())
            .append(this.getValue())
            .append(this.getUpdatedAt())
            .build();
    }

}
