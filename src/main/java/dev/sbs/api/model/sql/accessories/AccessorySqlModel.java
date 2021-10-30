package dev.sbs.api.model.sql.accessories;

import dev.sbs.api.data.sql.converter.ObjectMapConverter;
import dev.sbs.api.data.sql.model.SqlEffectsModel;
import dev.sbs.api.data.sql.model.SqlModel;
import dev.sbs.api.model.AccessoryModel;
import dev.sbs.api.model.sql.accessories.accessoryfamilies.AccessoryFamilySqlModel;
import dev.sbs.api.model.sql.items.ItemSqlModel;
import dev.sbs.api.model.sql.rarities.RaritySqlModel;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "accessories")
public class AccessorySqlModel extends SqlEffectsModel implements AccessoryModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemSqlModel item;

    @Getter
    @Setter
    @Column(name = "name", nullable = false, length = 127)
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

        if (id != that.id) return false;
        if (familyRank != that.familyRank) return false;
        if (!item.equals(that.item)) return false;
        if (!Objects.equals(family, that.family)) return false;
        if (!name.equals(that.name)) return false;
        if (!rarity.equals(that.rarity)) return false;
        if (!effects.equals(that.effects)) return false;
        return updatedAt.equals(that.updatedAt);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
