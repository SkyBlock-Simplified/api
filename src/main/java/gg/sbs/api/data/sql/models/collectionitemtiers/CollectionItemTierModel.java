package gg.sbs.api.data.sql.models.collectionitemtiers;

import gg.sbs.api.data.sql.SqlModel;
import gg.sbs.api.data.sql.converters.StringListConverter;
import gg.sbs.api.data.sql.models.collectionitems.CollectionItemModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "collection_item_tiers")
public class CollectionItemTierModel implements SqlModel {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "collection_item", nullable = false)
    private CollectionItemModel collectionItem;

    @Getter
    @Setter
    @Column(name = "tier", nullable = false)
    private int tier;

    @Getter
    @Setter
    @Column(name = "amount_required", nullable = false)
    private double amountRequired;

    @Getter
    @Setter
    @Column(name = "unlocks", nullable = false)
    @Convert(converter = StringListConverter.class)
    private List<String> unlocks;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CollectionItemTierModel)) return false;

        CollectionItemTierModel that = (CollectionItemTierModel) o;

        if (id != that.id) return false;
        if (tier != that.tier) return false;
        if (Double.compare(that.amountRequired, amountRequired) != 0) return false;
        if (!collectionItem.equals(that.collectionItem)) return false;
        if (!unlocks.equals(that.unlocks)) return false;
        return updatedAt.equals(that.updatedAt);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + collectionItem.hashCode();
        result = 31 * result + tier;
        temp = Double.doubleToLongBits(amountRequired);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + unlocks.hashCode();
        result = 31 * result + updatedAt.hashCode();
        return result;
    }
}

