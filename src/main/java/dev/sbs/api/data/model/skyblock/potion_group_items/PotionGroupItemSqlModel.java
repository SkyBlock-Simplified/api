package dev.sbs.api.data.model.skyblock.potion_group_items;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.potion_groups.PotionGroupSqlModel;
import dev.sbs.api.data.model.skyblock.potion_tiers.PotionTierSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(
        name = "skyblock_potion_group_items",
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
    private Long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "potion_group_key", nullable = false)
    private PotionGroupSqlModel potionGroup;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "potion_key", nullable = false, referencedColumnName = "potion_key"),
            @JoinColumn(name = "potion_tier", nullable = false, referencedColumnName = "tier")
    })
    private PotionTierSqlModel potionTier;

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