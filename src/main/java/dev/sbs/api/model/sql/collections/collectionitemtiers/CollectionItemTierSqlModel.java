package dev.sbs.api.model.sql.collections.collectionitemtiers;

import dev.sbs.api.data.sql.converter.StringListConverter;
import dev.sbs.api.data.sql.model.SqlModel;
import dev.sbs.api.model.CollectionItemTierModel;
import dev.sbs.api.model.sql.collections.collectionitems.CollectionItemSqlModel;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "collection_item_tiers")
public class CollectionItemTierSqlModel implements CollectionItemTierModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "collection_item_key", nullable = false)
    private CollectionItemSqlModel collectionItem;

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
        if (!(o instanceof CollectionItemTierSqlModel)) return false;

        CollectionItemTierSqlModel that = (CollectionItemTierSqlModel) o;

        if (id != that.id) return false;
        if (tier != that.tier) return false;
        if (Double.compare(that.amountRequired, amountRequired) != 0) return false;
        if (!collectionItem.equals(that.collectionItem)) return false;
        if (!unlocks.equals(that.unlocks)) return false;
        return updatedAt.equals(that.updatedAt);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}

