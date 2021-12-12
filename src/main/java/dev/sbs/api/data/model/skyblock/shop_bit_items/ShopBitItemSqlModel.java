package dev.sbs.api.data.model.skyblock.shop_bit_items;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.items.ItemSqlModel;
import dev.sbs.api.data.model.skyblock.shop_bit_types.ShopBitTypeSqlModel;
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
    name = "skyblock_shop_bit_items",
    indexes = {
        @Index(
            columnList = "bit_type_key"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ShopBitItemSqlModel implements ShopBitItemModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Getter
    @Setter
    @Id
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemSqlModel item;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "bit_type_key", nullable = false)
    private ShopBitTypeSqlModel type;

    @Getter
    @Setter
    @Column(name = "bit_cost", nullable = false)
    private Integer bitCost;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShopBitItemSqlModel)) return false;
        ShopBitItemSqlModel that = (ShopBitItemSqlModel) o;

        return new EqualsBuilder().append(this.getId(), that.getId())
            .append(this.getItem(), that.getItem())
            .append(this.getType(), that.getType())
            .append(this.getBitCost(), that.getBitCost())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.getId()).append(this.getItem()).append(this.getType()).append(this.getBitCost()).append(this.getUpdatedAt()).build();
    }

}
