package dev.sbs.api.data.model.skyblock.potion_data.potion_tiers;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.items.ItemSqlModel;
import dev.sbs.api.data.model.skyblock.potion_data.potions.PotionSqlModel;
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
    name = "skyblock_potion_tiers",
    indexes = {
        @Index(
            columnList = "potion_key, tier",
            unique = true
        ),
        @Index(
            columnList = "tier"
        ),
        @Index(
            columnList = "ingredient_item_id"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PotionTierSqlModel implements PotionTierModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "potion_key", nullable = false)
    private PotionSqlModel potion;

    @Getter
    @Setter
    @Column(name = "tier", nullable = false)
    private Integer tier;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "ingredient_item_id")
    private ItemSqlModel ingredientItem;

    @Getter
    @Setter
    @Column(name = "base_item_id")
    private String baseItem;

    @Getter
    @Setter
    @Column(name = "exp_yield", nullable = false)
    private Integer experienceYield;

    @Getter
    @Setter
    @Column(name = "sell_price", nullable = false)
    private Double sellPrice;

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
        if (o == null || getClass() != o.getClass()) return false;

        PotionTierSqlModel that = (PotionTierSqlModel) o;

        return new EqualsBuilder()
            .append(this.getId(), that.getId())
            .append(this.getPotion(), that.getPotion())
            .append(this.getTier(), that.getTier())
            .append(this.getIngredientItem(), that.getIngredientItem())
            .append(this.getBaseItem(), that.getBaseItem())
            .append(this.getExperienceYield(), that.getExperienceYield())
            .append(this.getSellPrice(), that.getSellPrice())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getPotion())
            .append(this.getTier())
            .append(this.getIngredientItem())
            .append(this.getBaseItem())
            .append(this.getExperienceYield())
            .append(this.getSellPrice())
            .append(this.getUpdatedAt())
            .build();
    }

}