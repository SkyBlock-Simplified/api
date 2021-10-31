package dev.sbs.api.data.model.potion_tiers;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.items.ItemSqlModel;
import dev.sbs.api.data.model.potions.PotionSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "potion_tiers")
public class PotionTierSqlModel implements PotionTierModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "potion_key", nullable = false, referencedColumnName = "key")
    private PotionSqlModel potion;

    @Getter
    @Setter
    @Column(name = "tier", nullable = false)
    private int tier;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "ingredient_item_id", nullable = false, referencedColumnName = "item_id")
    private ItemSqlModel item;

    @Getter
    @Setter
    @Column(name = "experience_yield", nullable = false)
    private int experienceYield;

    @Getter
    @Setter
    @Column(name = "sell_cost", nullable = false)
    private int sellCost;

    @Getter
    @Setter
    @Column(name = "effects", nullable = false)
    private Map<String, Object> effects;

    @Getter
    @Setter
    @Column(name = "buff_effects", nullable = false)
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
