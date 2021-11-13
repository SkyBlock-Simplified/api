package dev.sbs.api.data.model.potion_tiers;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.items.ItemSqlModel;
import dev.sbs.api.data.model.potions.PotionSqlModel;
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
        name = "potion_tiers",
        indexes = {
                @Index(
                        columnList = "potion_key, tier",
                        unique = true
                ),
                @Index(
                        columnList = "tier"
                ),
                @Index(
                        columnList = "base_item_id"
                ),
                @Index(
                        columnList = "ingredient_item_id"
                )
        }
)
public class PotionTierSqlModel implements PotionTierModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "potion_key", nullable = false)
    private PotionSqlModel potion;

    @Getter
    @Setter
    @Column(name = "tier", nullable = false)
    private int tier;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_item_id")
    private ItemSqlModel ingredientItem;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_item_id")
    private ItemSqlModel baseItem;

    @Getter
    @Setter
    @Column(name = "exp_yield", nullable = false)
    private int experienceYield;

    @Getter
    @Setter
    @Column(name = "sell_price", nullable = false)
    private double sellPrice;

    @Getter
    @Setter
    @Column(name = "effects", nullable = false)
    @Convert(converter = DoubleMapConverter.class)
    private Map<String, Double> effects;

    @Getter
    @Setter
    @Column(name = "buff_effects", nullable = false)
    @Convert(converter = DoubleMapConverter.class)
    private Map<String, Double> buffEffects;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    @SuppressWarnings("all")
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
