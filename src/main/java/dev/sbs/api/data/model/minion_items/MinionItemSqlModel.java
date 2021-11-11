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
    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "minion_key")
    private MinionSqlModel minion;

    @Getter
    @Setter
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_item_id")
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
    @SuppressWarnings("all")
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
