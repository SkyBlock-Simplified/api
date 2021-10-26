package dev.sbs.api.model.sql.collections.collectionitems;

import dev.sbs.api.data.sql.model.SqlModel;
import dev.sbs.api.model.CollectionItemModel;
import dev.sbs.api.model.sql.collections.CollectionSqlModel;
import dev.sbs.api.model.sql.items.ItemSqlModel;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "collection_items")
public class CollectionItemSqlModel implements CollectionItemModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "collection_key", nullable = false)
    private CollectionSqlModel collection;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemSqlModel item;

    @Getter
    @Setter
    @Column(name = "name", nullable = false, length = 127)
    private String name;

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

        if (id != that.id) return false;
        if (maxTiers != that.maxTiers) return false;
        if (!collection.equals(that.collection)) return false;
        if (!item.equals(that.item)) return false;
        if (!name.equals(that.name)) return false;
        return updatedAt.equals(that.updatedAt);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
