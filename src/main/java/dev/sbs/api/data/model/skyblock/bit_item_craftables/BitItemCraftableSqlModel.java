package dev.sbs.api.data.model.skyblock.bit_item_craftables;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.bit_items.BitItemSqlModel;
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
    name = "discord_bit_item_craftables",
    indexes = {
        @Index(
            columnList = "bit_item_id"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BitItemCraftableSqlModel implements BitItemCraftableModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Getter
    @Setter
    @JoinColumn(name = "bit_item_id", nullable = false)
    private BitItemSqlModel bitItem;

    @Getter
    @Setter
    @Id
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
    
}
