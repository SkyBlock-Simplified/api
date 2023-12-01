package dev.sbs.api.data.model.skyblock.bonus_data.bonus_armor_sets;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.items.ItemSqlModel;
import dev.sbs.api.data.sql.converter.map.StringDoubleMapConverter;
import dev.sbs.api.data.sql.converter.map.StringObjectMapConverter;
import dev.sbs.api.util.builder.hash.EqualsBuilder;
import dev.sbs.api.util.builder.hash.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.Map;

@Getter
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Setter
    @Column(name = "key", nullable = false, unique = true)
    private String key;

    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Setter
    @ManyToOne
    @JoinColumn(name = "helmet_item_id", nullable = false)
    private ItemSqlModel helmetItem;

    @Setter
    @ManyToOne
    @JoinColumn(name = "chestplate_item_id", nullable = false)
    private ItemSqlModel chestplateItem;

    @Setter
    @ManyToOne
    @JoinColumn(name = "leggings_item_id", nullable = false)
    private ItemSqlModel leggingsItem;

    @Setter
    @ManyToOne
    @JoinColumn(name = "boots_item_id", nullable = false)
    private ItemSqlModel bootsItem;

    @Setter
    @Column(name = "effects", nullable = false)
    @Convert(converter = StringDoubleMapConverter.class)
    private Map<String, Double> effects;

    @Setter
    @Column(name = "buff_effects", nullable = false)
    @Convert(converter = StringObjectMapConverter.class)
    private Map<String, Object> buffEffects;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @CreationTimestamp
    @Column(name = "submitted_at", nullable = false)
    private Instant submittedAt;

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
            .append(this.getEffects(), that.getEffects())
            .append(this.getBuffEffects(), that.getBuffEffects())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .append(this.getSubmittedAt(), that.getSubmittedAt())
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
            .append(this.getEffects())
            .append(this.getBuffEffects())
            .append(this.getUpdatedAt())
            .append(this.getSubmittedAt())
            .build();
    }

}
