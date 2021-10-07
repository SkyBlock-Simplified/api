package gg.sbs.api.data.sql.model.collectionitems;

import gg.sbs.api.data.sql.model.SqlModel;
import gg.sbs.api.data.sql.model.collections.CollectionModel;
import gg.sbs.api.util.builder.HashCodeBuilder;
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
        return new HashCodeBuilder().append(this.id).append(this.collection).append(this.itemKey).append(this.name).append(this.maxTiers).append(this.updatedAt).build();
    }

}
