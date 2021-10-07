package gg.sbs.api.data.sql.model.miniontiers;

import gg.sbs.api.data.sql.model.SqlModel;
import gg.sbs.api.data.sql.model.items.ItemModel;
import gg.sbs.api.data.sql.model.minions.MinionModel;
import gg.sbs.api.util.builder.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "minion_tiers")
public class MinionTierModel implements SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "minion", nullable = false)
    private MinionModel minion;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "tier", nullable = false)
    private ItemModel tier;

    @Getter
    @Setter
    @Column(name = "speed", nullable = false)
    private int speed;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MinionTierModel)) return false;

        MinionTierModel that = (MinionTierModel) o;

        if (id != that.id) return false;
        if (!minion.equals(that.minion)) return false;
        if (!tier.equals(that.tier)) return false;
        return updatedAt.equals(that.updatedAt);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.id).append(this.minion).append(this.tier).append(this.updatedAt).build();
    }

}