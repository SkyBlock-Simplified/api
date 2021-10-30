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
import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "accessories")
public class AccessorySqlModel implements AccessoryModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false, referencedColumnName = "item_id")
    private ItemSqlModel item;

    @Getter
    @Setter
    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "rarity_key", nullable = false, referencedColumnName = "key")
    private RaritySqlModel rarity;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "family_key", referencedColumnName = "key")
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
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
        /*if (this == o) return true;
        if (!(o instanceof AccessorySqlModel)) return false;

        AccessorySqlModel that = (AccessorySqlModel) o;

        if (id != that.id) return false;
        if (familyRank != that.familyRank) return false;
        if (!item.equals(that.item)) return false;
        if (!Objects.equals(family, that.family)) return false;
        if (!name.equals(that.name)) return false;
        if (!rarity.equals(that.rarity)) return false;
        if (!effects.equals(that.effects)) return false;
        return updatedAt.equals(that.updatedAt);*/
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
