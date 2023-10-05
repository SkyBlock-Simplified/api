package dev.sbs.api.data.model.skyblock.shop_data.shop_bit_item_craftables;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.items.ItemSqlModel;
import dev.sbs.api.data.model.skyblock.shop_data.shop_bit_items.ShopBitItemSqlModel;
import dev.sbs.api.util.builder.hash.EqualsBuilder;
import dev.sbs.api.util.builder.hash.HashCodeBuilder;
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
    name = "skyblock_shop_bit_item_craftables",
    indexes = {
        @Index(
            columnList = "bit_item_id"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ShopBitItemCraftableSqlModel implements ShopBitItemCraftableModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "bit_item_id", nullable = false)
    private ShopBitItemSqlModel bitItem;

    @Getter
    @Setter
    @Id
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemSqlModel craftableItem;

    @Getter
    @Setter
    @Column(name = "description", nullable = false)
    private String description;

    @Getter
    @Setter
    @Column(name = "expression", nullable = false)
    private String expression;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShopBitItemCraftableSqlModel that = (ShopBitItemCraftableSqlModel) o;

        return new EqualsBuilder()
            .append(this.getId(), that.getId())
            .append(this.getBitItem(), that.getBitItem())
            .append(this.getCraftableItem(), that.getCraftableItem())
            .append(this.getDescription(), that.getDescription())
            .append(this.getExpression(), that.getExpression())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getBitItem())
            .append(this.getCraftableItem())
            .append(this.getDescription())
            .append(this.getExpression())
            .append(this.getUpdatedAt())
            .build();
    }

}
