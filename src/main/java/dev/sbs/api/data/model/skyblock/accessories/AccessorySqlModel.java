package dev.sbs.api.data.model.skyblock.accessories;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.accessory_families.AccessoryFamilySqlModel;
import dev.sbs.api.data.model.skyblock.items.ItemSqlModel;
import dev.sbs.api.data.model.skyblock.rarities.RaritySqlModel;
import dev.sbs.api.data.sql.converter.map.StringDoubleMapConverter;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(
    name = "skyblock_accessories",
    indexes = {
        @Index(
            columnList = "family_key"
        ),
        @Index(
            columnList = "rarity_key"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AccessorySqlModel implements AccessoryModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Getter
    @Setter
    @Id
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemSqlModel item;

    @Getter
    @Setter
    @Column(name = "name", nullable = false, length = 256)
    private String name;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "rarity_key", nullable = false)
    private RaritySqlModel rarity;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "family_key")
    private AccessoryFamilySqlModel family;

    @Getter
    @Setter
    @Column(name = "family_rank")
    private Integer familyRank;

    @Getter
    @Setter
    @Column(name = "effects", nullable = false)
    @Convert(converter = StringDoubleMapConverter.class)
    private Map<String, Double> effects;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccessorySqlModel that = (AccessorySqlModel) o;

        return new EqualsBuilder()
            .append(this.getId(), that.getId())
            .append(this.getItem(), that.getItem())
            .append(this.getName(), that.getName())
            .append(this.getRarity(), that.getRarity())
            .append(this.getFamily(), that.getFamily())
            .append(this.getFamilyRank(), that.getFamilyRank())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getItem())
            .append(this.getName())
            .append(this.getRarity())
            .append(this.getFamily())
            .append(this.getFamilyRank())
            .append(this.getUpdatedAt())
            .build();
    }

}
