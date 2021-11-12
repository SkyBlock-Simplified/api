package dev.sbs.api.data.model.collection_items;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.collections.CollectionSqlModel;
import dev.sbs.api.data.model.items.ItemSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.Instant;

@Entity
@Table(name = "collection_items")
public class CollectionItemSqlModel implements CollectionItemModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_key", nullable = false)
    private CollectionSqlModel collection;

    @Getter
    @Setter
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false, unique = true)
    private ItemSqlModel item;

    @Getter
    @Setter
    @Column(name = "max_tiers", nullable = false)
    private int maxTiers;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CollectionItemSqlModel)) return false;
        CollectionItemSqlModel that = (CollectionItemSqlModel) o;

        return new EqualsBuilder()
                .append(this.getId(), that.getId())
                .append(this.getMaxTiers(), that.getMaxTiers())
                .append(this.getCollection(), that.getCollection())
                .append(this.getItem(), that.getItem())
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
                .append(this.getUpdatedAt())
                .build();
    }

}
