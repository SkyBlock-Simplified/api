package dev.sbs.api.model.sql.reforges;

import dev.sbs.api.data.sql.converter.ObjectMapConverter;
import dev.sbs.api.data.sql.model.SqlEffectsModel;
import dev.sbs.api.model.ReforgeModel;
import dev.sbs.api.model.sql.items.itemtypes.ItemTypeSqlModel;
import dev.sbs.api.model.sql.rarities.RaritySqlModel;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "reforges")
public class ReforgeSqlModel extends SqlEffectsModel implements ReforgeModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "item_type_key", nullable = false)
    private ItemTypeSqlModel itemType;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "rarity", nullable = false)
    private RaritySqlModel rarity;

    @Getter
    @Setter
    @Column(name = "is_stone", nullable = false)
    private boolean stone;

    @Getter
    @Setter
    @Column(name = "effects")
    @Convert(converter = ObjectMapConverter.class)
    private Map<String, Object> effects;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReforgeSqlModel)) return false;

        ReforgeSqlModel that = (ReforgeSqlModel) o;

        if (id != that.id) return false;
        if (stone != that.stone) return false;
        if (!itemType.equals(that.itemType)) return false;
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
