package dev.sbs.api.data.model.skyblock_bag_sizes;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.collection_items.CollectionItemSqlModel;
import dev.sbs.api.data.model.skyblock_bags.SkyBlockBagSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.Instant;

@Entity
@Table(
        name = "skyblock_bag_sizes",
        indexes = {
                @Index(
                        columnList = "bag_key, collection_tier",
                        unique = true
                )
        }
)
public class SkyBlockBagSizeSqlModel implements SkyBlockBagSizeModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bag_key", nullable = false)
    private SkyBlockBagSqlModel bag;

    @Getter
    @Setter
    @Column(name = "collection_tier", nullable = false, length = 127)
    private int collectionTier;

    @Getter
    @Setter
    @Column(name = "slot_count", nullable = false)
    private int slotCount;

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
