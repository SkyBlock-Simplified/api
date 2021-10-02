package gg.sbs.api.data.sql.models.accessories;

import gg.sbs.api.data.sql.SqlJsonConverter;
import gg.sbs.api.data.sql.SqlModel;
import gg.sbs.api.data.sql.models.SqlEffectsModel;
import gg.sbs.api.data.sql.models.accessoryfamilies.AccessoryFamilyModel;
import gg.sbs.api.data.sql.models.rarities.RarityModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "accessories")
public class AccessoryModel extends SqlEffectsModel implements SqlModel {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @Column(name = "item_id", nullable = false, length = 127)
    private String itemId;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "family")
    private AccessoryFamilyModel family;

    @Getter
    @Setter
    @Column(name = "family_rank")
    private int familyRank;

    @Getter
    @Setter
    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "rarity", nullable = false)
    private RarityModel rarity;

    @Getter
    @Setter
    @Column(name = "effects", nullable = false)
    @Convert(converter = SqlJsonConverter.class)
    private Map<String, Object> effects;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccessoryModel)) return false;

        AccessoryModel that = (AccessoryModel) o;

        if (id != that.id) return false;
        if (familyRank != that.familyRank) return false;
        if (!itemId.equals(that.itemId)) return false;
        if (!Objects.equals(family, that.family)) return false;
        if (!name.equals(that.name)) return false;
        if (!rarity.equals(that.rarity)) return false;
        if (!effects.equals(that.effects)) return false;
        return updatedAt.equals(that.updatedAt);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + itemId.hashCode();
        result = 31 * result + (family != null ? family.hashCode() : 0);
        result = 31 * result + familyRank;
        result = 31 * result + name.hashCode();
        result = 31 * result + rarity.hashCode();
        result = 31 * result + effects.hashCode();
        result = 31 * result + updatedAt.hashCode();
        return result;
    }
}
