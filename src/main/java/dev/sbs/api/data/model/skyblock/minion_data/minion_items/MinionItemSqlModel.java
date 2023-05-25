package dev.sbs.api.data.model.skyblock.minion_data.minion_items;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.collection_data.collection_items.CollectionItemSqlModel;
import dev.sbs.api.data.model.skyblock.items.ItemSqlModel;
import dev.sbs.api.data.model.skyblock.minion_data.minions.MinionSqlModel;
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
    name = "skyblock_minion_items",
    indexes = {
        @Index(
            columnList = "minion_key"
        ),
        @Index(
            columnList = "collection_item_id"
        ),
        @Index(
            columnList = "item_id"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MinionItemSqlModel implements MinionItemModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "minion_key", nullable = false)
    private MinionSqlModel minion;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "collection_item_id")
    private CollectionItemSqlModel collectionItem;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemSqlModel item;

    @Getter
    @Setter
    @Column(name = "average_yield", nullable = false)
    private Double averageYield;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MinionItemSqlModel that = (MinionItemSqlModel) o;

        return new EqualsBuilder()
            .append(this.getId(), that.getId())
            .append(this.getMinion(), that.getMinion())
            .append(this.getCollectionItem(), that.getCollectionItem())
            .append(this.getItem(), that.getItem())
            .append(this.getAverageYield(), that.getAverageYield())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getMinion())
            .append(this.getCollectionItem())
            .append(this.getItem())
            .append(this.getAverageYield())
            .append(this.getUpdatedAt())
            .build();
    }

}
