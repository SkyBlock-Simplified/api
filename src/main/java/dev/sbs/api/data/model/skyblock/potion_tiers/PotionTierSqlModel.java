package dev.sbs.api.data.model.skyblock.potion_tiers;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.items.ItemSqlModel;
import dev.sbs.api.data.model.skyblock.potions.PotionSqlModel;
import dev.sbs.api.data.sql.converter.ObjectMapConverter;
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
    @Convert(converter = ObjectMapConverter.class)
    private Map<String, Object> effects;

    @Getter
    @Setter
    @Column(name = "buff_effects", nullable = false)
    @Convert(converter = ObjectMapConverter.class)
    private Map<String, Object> buffEffects;

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