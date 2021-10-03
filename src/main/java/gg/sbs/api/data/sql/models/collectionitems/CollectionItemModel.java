package gg.sbs.api.data.sql.models.collectionitems;

import gg.sbs.api.data.sql.SqlModel;
import gg.sbs.api.data.sql.models.collections.CollectionModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "collection_items")
public class CollectionItemModel implements SqlModel {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "collection", nullable = false)
    private CollectionModel collection;

    @Getter
    @Setter
    @Column(name = "item_key", nullable = false, length = 127)
    private String itemKey;

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
        if (!(o instanceof CollectionItemModel)) return false;

        CollectionItemModel that = (CollectionItemModel) o;

        if (id != that.id) return false;
        if (maxTiers != that.maxTiers) return false;
        if (!collection.equals(that.collection)) return false;
        if (!itemKey.equals(that.itemKey)) return false;
        if (!name.equals(that.name)) return false;
        return updatedAt.equals(that.updatedAt);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + collection.hashCode();
        result = 31 * result + itemKey.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + maxTiers;
        result = 31 * result + updatedAt.hashCode();
        return result;
    }
}
