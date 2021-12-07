package dev.sbs.api.data.model.skyblock.bit_items;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.bit_types.BitTypeSqlModel;
import dev.sbs.api.data.model.skyblock.items.ItemSqlModel;
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
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(
    name = "discord_bit_items",
    indexes = {
        @Index(
            columnList = "bit_type_key"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BitItemSqlModel implements BitItemModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Getter
    @Setter
    @Id
    @JoinColumn(name = "item_id", nullable = false)
    private ItemSqlModel item;

    @Getter
    @Setter
    @JoinColumn(name = "bit_type_key", nullable = false)
    private BitTypeSqlModel type;

    @Getter
    @Setter
    @Column(name = "bit_cost", nullable = false)
    private Integer bitCost;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
    
}
