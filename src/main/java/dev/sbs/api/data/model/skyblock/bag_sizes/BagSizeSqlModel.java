package dev.sbs.api.data.model.skyblock.bag_sizes;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.bags.BagSqlModel;
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
        name = "skyblock_bag_sizes",
        indexes = {
                @Index(
                        columnList = "bag_key, collection_tier",
                        unique = true
                )
        }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BagSizeSqlModel implements BagSizeModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "bag_key", nullable = false)
    private BagSqlModel bag;

    @Getter
    @Setter
    @Column(name = "collection_tier", nullable = false, length = 127)
    private Integer collectionTier;

    @Getter
    @Setter
    @Column(name = "slot_count", nullable = false)
    private Integer slotCount;

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
