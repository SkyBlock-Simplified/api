package dev.sbs.api.data.model.collection_item_tiers;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.collection_items.CollectionItemSqlModel;
import dev.sbs.api.data.sql.converter.StringListConverter;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(
        name = "collection_item_tiers",
        indexes = {
                @Index(
                        columnList = "collection_item_id, tier",
                        unique = true
                )
        }
)
public class CollectionItemTierSqlModel implements CollectionItemTierModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_item_id", nullable = false)
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

        return new EqualsBuilder().append(this.getId(), that.getId())
                .append(this.getTier(), that.getTier())
                .append(this.getAmountRequired(), that.getAmountRequired())
                .append(this.getCollectionItem(), that.getCollectionItem())
                .append(this.getUnlocks(), that.getUnlocks())
                .append(this.getUpdatedAt(), that.getUpdatedAt())
                .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.getId())
                .append(this.getCollectionItem())
                .append(this.getTier())
                .append(this.getAmountRequired())
                .append(this.getUnlocks())
                .append(this.getUpdatedAt())
                .build();
    }
    
}

