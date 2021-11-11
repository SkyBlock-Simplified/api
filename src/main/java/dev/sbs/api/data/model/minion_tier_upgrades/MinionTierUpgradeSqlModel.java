package dev.sbs.api.data.model.minion_tier_upgrades;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.collection_items.CollectionItemSqlModel;
import dev.sbs.api.data.model.minion_tiers.MinionTierSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "minion_tier_upgrades")
public class MinionTierUpgradeSqlModel implements MinionTierUpgradeModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "minion_tier", nullable = false)
    private MinionTierSqlModel minionTier;

    @Getter
    @Setter
    @Column(name = "coin_cost", nullable = false)
    private double coinCost;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_cost")
    private CollectionItemSqlModel itemCost;

    @Getter
    @Setter
    @Column(name = "item_quantity", nullable = false)
    private int itemQuantity;

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
