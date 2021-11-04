package dev.sbs.api.data.model.potion_group_items;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.potion_groups.PotionGroupSqlModel;
import dev.sbs.api.data.model.potion_tiers.PotionTierSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "potion_group_items")
public class PotionGroupItemSqlModel implements PotionGroupItemModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "potion_group_key", nullable = false, referencedColumnName = "key")
    private PotionGroupSqlModel potionGroup;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "potion_key", nullable = false, referencedColumnName = "potion_key")
    private PotionTierSqlModel potion;

    @Getter
    @Setter
    //@ManyToOne
    @Column(name = "tier", nullable = false)
    //@JoinColumn(name = "potion_tier", nullable = false, referencedColumnName = "tier")
    private int tier;

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
