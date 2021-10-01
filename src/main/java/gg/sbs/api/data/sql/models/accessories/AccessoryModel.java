package gg.sbs.api.data.sql.models.accessories;

import gg.sbs.api.data.sql.SqlJsonConverter;
import gg.sbs.api.data.sql.SqlModel;
import gg.sbs.api.data.sql.models.SqlEffectsModel;
import gg.sbs.api.data.sql.models.accessoryfamilies.AccessoryFamilyModel;
import gg.sbs.api.data.sql.models.rarities.RarityModel;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "accessories")
public class AccessoryModel extends SqlEffectsModel implements SqlModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "item_id", nullable = false, length = 127)
    private String itemId;

    @ManyToOne
    @JoinColumn(name = "family", nullable = true)
    private AccessoryFamilyModel family;

    @Column(name = "family_rank", nullable = true)
    private int familyRank;

    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @ManyToOne
    @JoinColumn(name = "rarity", nullable = false)
    private RarityModel rarity;

    @Column(name = "effects")
    @Convert(converter = SqlJsonConverter.class)
    private Map<String, Object> effects;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public long getId() {
        return id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public AccessoryFamilyModel getFamily() {
        return family;
    }

    public void setFamily(AccessoryFamilyModel family) {
        this.family = family;
    }

    public int getFamilyRank() {
        return familyRank;
    }

    public void setFamilyRank(int familyRank) {
        this.familyRank = familyRank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RarityModel getRarity() {
        return rarity;
    }

    public void setRarity(RarityModel rarity) {
        this.rarity = rarity;
    }

    public Map<String, Object> getEffects() {
        return effects;
    }

    public void setEffects(Map<String, Object> effects) {
        this.effects = effects;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public boolean equals(Object o) {
        if (!(o instanceof AccessoryModel)) return false;
        return id == ((AccessoryModel) o).id;
    }

    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (family != null ? family.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (rarity != null ? rarity.hashCode() : 0);
        result = 31 * result + (effects != null ? effects.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        return result;
    }
}
