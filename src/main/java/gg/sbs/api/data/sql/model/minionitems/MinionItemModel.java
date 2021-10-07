package gg.sbs.api.data.sql.model.minionitems;

import gg.sbs.api.data.sql.model.SqlModel;
import gg.sbs.api.data.sql.model.collectionitems.CollectionItemModel;
import gg.sbs.api.data.sql.model.minions.MinionModel;
import gg.sbs.api.util.builder.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "minion_items")
public class MinionItemModel implements SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "minion")
    private MinionModel minion;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "collection_item_key")
    private CollectionItemModel collectionItem;

    @Getter
    @Setter
    @ManyToOne
    @Column(name = "yield", nullable = false)
    private double yield;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MinionItemModel)) return false;

        MinionItemModel that = (MinionItemModel) o;

        if (id != that.id) return false;
        if (!minion.equals(that.minion)) return false;
        if (!collectionItem.equals(that.collectionItem)) return false;
        return updatedAt.equals(that.updatedAt);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.id).append(this.minion).append(this.collectionItem).append(this.updatedAt).build();
    }

}