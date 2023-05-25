package dev.sbs.api.data.model.skyblock.minion_data.minion_tier_upgrades;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.items.ItemSqlModel;
import dev.sbs.api.data.model.skyblock.minion_data.minion_tiers.MinionTierSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(
    name = "skyblock_minion_tier_upgrades",
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
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MinionTierUpgradeSqlModel implements MinionTierUpgradeModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "minion_tier", nullable = false)
    private MinionTierSqlModel minionTier;

    @Getter
    @Setter
    @Column(name = "coin_cost", nullable = false)
    private Double coinCost;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "item_cost")
    private ItemSqlModel itemCost;

    @Getter
    @Setter
    @Column(name = "item_quantity", nullable = false)
    private Integer itemQuantity;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MinionTierUpgradeSqlModel that = (MinionTierUpgradeSqlModel) o;

        return new EqualsBuilder()
            .append(this.getId(), that.getId())
            .append(this.getMinionTier(), that.getMinionTier())
            .append(this.getCoinCost(), that.getCoinCost())
            .append(this.getItemCost(), that.getItemCost())
            .append(this.getItemQuantity(), that.getItemQuantity())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getMinionTier())
            .append(this.getCoinCost())
            .append(this.getItemCost())
            .append(this.getItemQuantity())
            .append(this.getUpdatedAt())
            .build();
    }

}
