package dev.sbs.api.data.model.pet_item_stats;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.items.ItemSqlModel;
import dev.sbs.api.data.model.stats.StatSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "pet_item_stats")
public class PetItemStatSqlModel implements PetItemStatModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false, referencedColumnName = "item_id")
    private ItemSqlModel item;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "stat_key", nullable = false, referencedColumnName = "key")
    private StatSqlModel stat;

    @Getter
    @Setter
    @Column(name = "stat_value", nullable = false)
    private int statValue;

    @Getter
    @Setter
    @Column(name = "percentage", nullable = false)
    private boolean percentage;

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
