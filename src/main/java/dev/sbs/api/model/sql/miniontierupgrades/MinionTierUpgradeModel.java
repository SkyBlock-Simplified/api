package dev.sbs.api.model.sql.miniontierupgrades;

import dev.sbs.api.data.sql.model.SqlModel;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import dev.sbs.api.model.sql.collectionitems.CollectionItemModel;
import dev.sbs.api.model.sql.miniontiers.MinionTierModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "minion_tier_upgrades")
public class MinionTierUpgradeModel implements SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "minion_tier", nullable = false)
    private MinionTierModel minionTier;

    @Getter
    @Setter
    @Column(name = "coin_cost", nullable = false)
    private int coinCost;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "item_cost")
    private CollectionItemModel itemCost;

    @Getter
    @Setter
    @Column(name = "quantity", nullable = false)
    private int itemQuantity;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MinionTierUpgradeModel)) return false;

        MinionTierUpgradeModel that = (MinionTierUpgradeModel) o;

        if (id != that.id) return false;
        if (!minionTier.equals(that.minionTier)) return false;
        if (coinCost != that.coinCost) return false;
        if (itemCost != that.itemCost) return false;
        if (itemQuantity != that.itemQuantity) return false;
        return updatedAt.equals(that.updatedAt);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
