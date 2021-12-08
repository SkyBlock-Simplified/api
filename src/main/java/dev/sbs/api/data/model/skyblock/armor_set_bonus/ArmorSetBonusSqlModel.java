package dev.sbs.api.data.model.skyblock.armor_set_bonus;

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

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(
    name = "skyblock_armor_set_bonus",
    indexes = {
        @Index(
            columnList = "helmet_item_id"
        ),
        @Index(
            columnList = "chestplate_item_id"
        ),
        @Index(
            columnList = "leggings_item_id"
        ),
        @Index(
            columnList = "boots_item_id"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ArmorSetBonusSqlModel implements ArmorSetBonusModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Getter
    @Setter
    @Id
    @Column(name = "key", nullable = false)
    private String key;

    @Getter
    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Getter
    @Setter
    @JoinColumn(name = "helmet_item_id", nullable = false)
    private ItemSqlModel helmetItem;

    @Getter
    @Setter
    @JoinColumn(name = "chestplate_item_id", nullable = false)
    private ItemSqlModel chestplateItem;

    @Getter
    @Setter
    @JoinColumn(name = "leggings_item_id", nullable = false)
    private ItemSqlModel leggingsItem;

    @Getter
    @Setter
    @JoinColumn(name = "boots_item_id", nullable = false)
    private ItemSqlModel bootsItem;

    @Getter
    @Setter
    @Column(name = "effects", nullable = false)
    @Convert(converter = StringDoubleMapConverter.class)
    private Map<String, Double> effects;

    @Getter
    @Setter
    @Column(name = "buff_effects", nullable = false)
    @Convert(converter = StringDoubleMapConverter.class)
    private Map<String, Double> buffEffects;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArmorSetBonusSqlModel)) return false;
        ArmorSetBonusSqlModel that = (ArmorSetBonusSqlModel) o;

        return new EqualsBuilder().append(this.getId(), that.getId())
            .append(this.getKey(), that.getKey())
            .append(this.getName(), that.getName())
            .append(this.getHelmetItem(), that.getHelmetItem())
            .append(this.getChestplateItem(), that.getChestplateItem())
            .append(this.getLeggingsItem(), that.getLeggingsItem())
            .append(this.getBootsItem(), that.getBootsItem())
            .append(this.getEffects(), that.getEffects())
            .append(this.getBuffEffects(), that.getBuffEffects())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.getId())
            .append(this.getKey())
            .append(this.getName())
            .append(this.getHelmetItem())
            .append(this.getChestplateItem())
            .append(this.getLeggingsItem())
            .append(this.getBootsItem())
            .append(this.getEffects())
            .append(this.getBuffEffects())
            .append(this.getUpdatedAt())
            .build();
    }

}
