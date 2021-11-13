package dev.sbs.api.data.model.minion_tier_upgrades;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.items.ItemSqlModel;
import dev.sbs.api.data.model.minion_tiers.MinionTierSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(
        name = "minion_tier_upgrades",
        indexes = {
                @Index(
                        columnList = "minion_tier, item_cost",
                        unique = true
                ),
                @Index(
                        columnList = "minion_tier"
                ),
                @Index(
                        columnList = "item_cost"
                )
        }
)
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
    private ItemSqlModel itemCost;

    @Getter
    @Setter
    @Column(name = "item_quantity", nullable = false)
    private int itemQuantity;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MinionTierUpgradeSqlModel)) return false;
        MinionTierUpgradeSqlModel that = (MinionTierUpgradeSqlModel) o;

        return new EqualsBuilder()
                .append(this.getId(), that.getId())
                .append(this.getCoinCost(), that.getCoinCost())
                .append(this.getItemQuantity(), that.getItemQuantity())
                .append(this.getMinionTier(), that.getMinionTier())
                .append(this.getItemCost(), that.getItemCost())
                .append(this.getUpdatedAt(), that.getUpdatedAt())
                .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.getId())
                .append(this.getMinionTier())
                .append(this.getCoinCost())
                .append(this.getItemCost())
                .append(this.getItemQuantity())
                .append(this.getUpdatedAt())
                .build();
    }
    
}
