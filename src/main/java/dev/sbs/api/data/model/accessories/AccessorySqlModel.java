package dev.sbs.api.data.model.accessories;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.accessory_families.AccessoryFamilySqlModel;
import dev.sbs.api.data.model.items.ItemSqlModel;
import dev.sbs.api.data.model.rarities.RaritySqlModel;
import dev.sbs.api.data.sql.converter.ObjectMapConverter;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "accessories")
public class AccessorySqlModel implements AccessoryModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false, unique = true)
    private ItemSqlModel item;

    @Getter
    @Setter
    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rarity_key", nullable = false)
    private RaritySqlModel rarity;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_key")
    private AccessoryFamilySqlModel family;

    @Getter
    @Setter
    @Column(name = "family_rank")
    private int familyRank;

    @Getter
    @Setter
    @Column(name = "effects", nullable = false)
    @Convert(converter = ObjectMapConverter.class)
    private Map<String, Object> effects;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccessorySqlModel)) return false;
        AccessorySqlModel that = (AccessorySqlModel) o;

        return new EqualsBuilder().append(this.getId(), that.getId())
                .append(this.getFamilyRank(), that.getFamilyRank())
                .append(this.getItem(), that.getItem())
                .append(this.getName(), that.getName())
                .append(this.getRarity(), that.getRarity())
                .append(this.getFamily(), that.getFamily())
                .append(this.getEffects(), that.getEffects())
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
                .append(this.getEffects())
                .append(this.getUpdatedAt())
                .build();
    }
    
}
