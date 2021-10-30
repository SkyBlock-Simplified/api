package dev.sbs.api.model.sql.minions.miniontierupgrades;

import dev.sbs.api.data.sql.model.SqlModel;
import dev.sbs.api.model.sql.collections.collectionitems.CollectionItemSqlModel;
import dev.sbs.api.model.sql.minions.miniontiers.MinionTierSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "minion_tier_upgrades")
public class MinionTierUpgradeSqlModel implements SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "minion_tier", nullable = false)
    private MinionTierSqlModel minionTier;

    @Getter
    @Setter
    @Column(name = "coin_cost", nullable = false)
    private int coinCost;

    @Getter
    @Setter
    @ManyToOne
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
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
        /*if (this == o) return true;
        if (!(o instanceof MinionTierUpgradeSqlModel)) return false;

        MinionTierUpgradeSqlModel that = (MinionTierUpgradeSqlModel) o;

        if (id != that.id) return false;
        if (!minionTier.equals(that.minionTier)) return false;
        if (coinCost != that.coinCost) return false;
        if (itemCost != that.itemCost) return false;
        if (itemQuantity != that.itemQuantity) return false;
        return updatedAt.equals(that.updatedAt);*/
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
