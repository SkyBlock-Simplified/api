package dev.sbs.api.data.model.skyblock_sack_items;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.items.ItemSqlModel;
import dev.sbs.api.data.model.skyblock_sacks.SkyBlockSackSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(
        name = "skyblock_sack_items",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "sack_item",
                        columnNames = { "sack_key", "item_id" }
                )
        }
)
public class SkyBlockSackItemSqlModel implements SkyBlockSackItemModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "sack_key", nullable = false, referencedColumnName = "key")
    private SkyBlockSackSqlModel sack;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false, referencedColumnName = "item_id")
    private ItemSqlModel item;

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