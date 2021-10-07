package gg.sbs.api.data.sql.model.minions;

import gg.sbs.api.data.sql.model.SqlModel;
import gg.sbs.api.data.sql.model.collections.CollectionModel;
import gg.sbs.api.util.StringUtil;
import gg.sbs.api.util.builder.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "minions")
public class MinionModel implements SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @Column(name = "item_id", nullable = false, length = 127)
    private String itemId;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "collection")
    private CollectionModel collection;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MinionModel)) return false;

        MinionModel that = (MinionModel) o;

        if (id != that.id) return false;
        if (!StringUtil.equals(itemId, that.itemId)) return false;
        if (collection != that.collection) return false;
        return updatedAt.equals(that.updatedAt);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.id).append(this.itemId).append(this.collection).append(this.updatedAt).build();
    }

}