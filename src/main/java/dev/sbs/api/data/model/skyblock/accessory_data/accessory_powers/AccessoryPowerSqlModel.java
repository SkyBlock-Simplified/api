package dev.sbs.api.data.model.skyblock.accessory_data.accessory_powers;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.items.ItemSqlModel;
import dev.sbs.api.data.sql.converter.map.StringDoubleMapConverter;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
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
    name = "skyblock_accessory_powers",
    indexes = {
        @Index(
            columnList = "item_id"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AccessoryPowerSqlModel implements AccessoryPowerModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Getter
    @Setter
    @Id
    @Column(name = "key", nullable = false, length = 256)
    private String key;

    @Getter
    @Setter
    @Id
    @Column(name = "name", nullable = false, length = 256)
    private String name;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemSqlModel item;

    @Getter
    @Setter
    @Column(name = "min_combat_level", nullable = false)
    private Integer minimumCombatLevel;

    @Getter
    @Setter
    @Column(name = "effects", nullable = false)
    @Convert(converter = StringDoubleMapConverter.class)
    private Map<String, Double> effects;

    @Getter
    @Setter
    @Column(name = "unique_effects", nullable = false)
    @Convert(converter = StringDoubleMapConverter.class)
    private Map<String, Double> uniqueEffects;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccessoryPowerSqlModel that = (AccessoryPowerSqlModel) o;

        return new EqualsBuilder()
            .append(this.isStone(), that.isStone())
            .append(this.getId(), that.getId())
            .append(this.getKey(), that.getKey())
            .append(this.getName(), that.getName())
            .append(this.getItem(), that.getItem())
            .append(this.getMinimumCombatLevel(), that.getMinimumCombatLevel())
            .append(this.getEffects(), that.getEffects())
            .append(this.getUniqueEffects(), that.getUniqueEffects())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getKey())
            .append(this.getName())
            .append(this.getItem())
            .append(this.getMinimumCombatLevel())
            .append(this.isStone())
            .append(this.getEffects())
            .append(this.getUniqueEffects())
            .append(this.getUpdatedAt())
            .build();
    }

}
