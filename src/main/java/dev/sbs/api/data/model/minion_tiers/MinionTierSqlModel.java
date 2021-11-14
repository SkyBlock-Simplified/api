package dev.sbs.api.data.model.minion_tiers;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.items.ItemSqlModel;
import dev.sbs.api.data.model.minions.MinionSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(
        name = "minion_tiers",
        indexes = {
                @Index(
                        columnList = "minion_key"
                )
        }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MinionTierSqlModel implements MinionTierModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "minion_key", nullable = false)
    private MinionSqlModel minion;

    @Getter
    @Setter
    @Id
    @ManyToOne
    @JoinColumn(name = "tier", nullable = false)
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MinionTierSqlModel)) return false;
        MinionTierSqlModel that = (MinionTierSqlModel) o;

        return new EqualsBuilder()
                .append(this.getId(), that.getId())
                .append(this.getSpeed(), that.getSpeed())
                .append(this.getMinion(), that.getMinion())
                .append(this.getItem(), that.getItem())
                .append(this.getUpdatedAt(), that.getUpdatedAt())
                .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.getId())
                .append(this.getMinion())
                .append(this.getItem())
                .append(this.getSpeed())
                .append(this.getUpdatedAt())
                .build();
    }

}
