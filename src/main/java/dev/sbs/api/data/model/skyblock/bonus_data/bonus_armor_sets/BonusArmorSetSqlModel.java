package dev.sbs.api.data.model.skyblock.bonus_data.bonus_armor_sets;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.items.ItemSqlModel;
import dev.sbs.api.data.sql.converter.map.StringDoubleMapConverter;
import dev.sbs.api.data.sql.converter.map.StringObjectMapConverter;
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
    name = "skyblock_bonus_armor_sets",
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
public class BonusArmorSetSqlModel implements BonusArmorSetModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
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
    @ManyToOne
    @JoinColumn(name = "helmet_item_id", nullable = false)
    private ItemSqlModel helmetItem;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "chestplate_item_id", nullable = false)
    private ItemSqlModel chestplateItem;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "leggings_item_id", nullable = false)
    private ItemSqlModel leggingsItem;

    @Getter
    @Setter
    @ManyToOne
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
    @Convert(converter = StringObjectMapConverter.class)
    private Map<String, Object> buffEffects;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BonusArmorSetSqlModel that = (BonusArmorSetSqlModel) o;

        return new EqualsBuilder()
            .append(this.getId(), that.getId())
            .append(this.getKey(), that.getKey())
            .append(this.getName(), that.getName())
            .append(this.getHelmetItem(), that.getHelmetItem())
            .append(this.getChestplateItem(), that.getChestplateItem())
            .append(this.getLeggingsItem(), that.getLeggingsItem())
            .append(this.getBootsItem(), that.getBootsItem())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getKey())
            .append(this.getName())
            .append(this.getHelmetItem())
            .append(this.getChestplateItem())
            .append(this.getLeggingsItem())
            .append(this.getBootsItem())
            .append(this.getUpdatedAt())
            .build();
    }

}
