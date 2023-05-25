package dev.sbs.api.data.model.skyblock.collection_data.collection_items;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.collection_data.collections.CollectionSqlModel;
import dev.sbs.api.data.model.skyblock.items.ItemSqlModel;
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
    name = "skyblock_collection_items",
    indexes = {
        @Index(
            columnList = "collection_key"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CollectionItemSqlModel implements CollectionItemModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "collection_key", nullable = false)
    private CollectionSqlModel collection;

    @Getter
    @Setter
    @Id
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemSqlModel item;

    @Getter
    @Setter
    @Column(name = "max_tiers", nullable = false)
    private Integer maxTiers;

    @Getter
    @Setter
    @Column(name = "farming_event", nullable = false)
    private boolean farmingEvent;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CollectionItemSqlModel that = (CollectionItemSqlModel) o;

        return new EqualsBuilder()
            .append(this.isFarmingEvent(), that.isFarmingEvent())
            .append(this.getId(), that.getId())
            .append(this.getCollection(), that.getCollection())
            .append(this.getItem(), that.getItem())
            .append(this.getMaxTiers(), that.getMaxTiers())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getCollection())
            .append(this.getItem())
            .append(this.getMaxTiers())
            .append(this.isFarmingEvent())
            .append(this.getUpdatedAt())
            .build();
    }

}
