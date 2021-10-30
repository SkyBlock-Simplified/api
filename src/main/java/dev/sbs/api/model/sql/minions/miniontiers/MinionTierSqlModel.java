package dev.sbs.api.model.sql.minions.miniontiers;

import dev.sbs.api.data.sql.model.SqlModel;
import dev.sbs.api.model.MinionTierModel;
import dev.sbs.api.model.sql.items.ItemSqlModel;
import dev.sbs.api.model.sql.minions.MinionSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "minion_tiers")
public class MinionTierSqlModel implements MinionTierModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "minion_key", nullable = false, referencedColumnName = "key")
    private MinionSqlModel minion;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "tier", nullable = false, referencedColumnName = "item_id")
    private ItemSqlModel item;

    @Getter
    @Setter
    @Column(name = "speed", nullable = false)
    private int speed;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
        /*if (this == o) return true;
        if (!(o instanceof MinionTierSqlModel)) return false;

        MinionTierSqlModel that = (MinionTierSqlModel) o;

        if (id != that.id) return false;
        if (!minion.equals(that.minion)) return false;
        if (!item.equals(that.item)) return false;
        return updatedAt.equals(that.updatedAt);*/
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
