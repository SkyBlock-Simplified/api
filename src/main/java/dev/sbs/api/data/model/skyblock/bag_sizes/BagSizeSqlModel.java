package dev.sbs.api.data.model.skyblock.bag_sizes;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.bags.BagSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UpdateTimestamp;

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
    @Column(name = "collection_tier", nullable = false, length = 256)
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BagSizeSqlModel that = (BagSizeSqlModel) o;

        return new EqualsBuilder()
            .append(this.getId(), that.getId())
            .append(this.getBag(), that.getBag())
            .append(this.getCollectionTier(), that.getCollectionTier())
            .append(this.getSlotCount(), that.getSlotCount())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getBag())
            .append(this.getCollectionTier())
            .append(this.getSlotCount())
            .append(this.getUpdatedAt())
            .build();
    }

}
