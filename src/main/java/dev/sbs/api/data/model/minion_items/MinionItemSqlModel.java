package dev.sbs.api.data.model.minion_items;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.collection_items.CollectionItemSqlModel;
import dev.sbs.api.data.model.minions.MinionSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "minion_items")
public class MinionItemSqlModel implements MinionItemModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "minion_key", referencedColumnName = "key")
    private MinionSqlModel minion;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "collection_item_id", referencedColumnName = "item_id")
    private CollectionItemSqlModel collectionItem;

    @Getter
    @Setter
    @Column(name = "average_yield", nullable = false)
    private double averageYield;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
        /*if (this == o) return true;
        if (!(o instanceof MinionItemSqlModel)) return false;

        MinionItemSqlModel that = (MinionItemSqlModel) o;

        if (id != that.id) return false;
        if (!minion.equals(that.minion)) return false;
        if (!collectionItem.equals(that.collectionItem)) return false;
        return updatedAt.equals(that.updatedAt);*/
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
