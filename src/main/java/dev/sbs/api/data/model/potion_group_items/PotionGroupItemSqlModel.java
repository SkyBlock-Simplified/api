package dev.sbs.api.data.model.potion_group_items;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.potion_groups.PotionGroupSqlModel;
import dev.sbs.api.data.model.potion_tiers.PotionTierSqlModel;
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
        name = "potion_group_items",
        indexes = {
                @Index(
                        columnList = "potion_key, potion_group_key",
                        unique = true
                ),
                @Index(
                        columnList = "potion_key, potion_tier"
                )
        }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PotionGroupItemSqlModel implements PotionGroupItemModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "potion_group_key", nullable = false)
    private PotionGroupSqlModel potionGroup;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "potion_key", nullable = false, referencedColumnName = "potion_key")
    private PotionTierSqlModel potion;

    @Getter
    @Setter
    @Column(name = "potion_tier", nullable = false)
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
