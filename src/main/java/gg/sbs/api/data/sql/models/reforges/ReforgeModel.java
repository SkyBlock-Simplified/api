package gg.sbs.api.data.sql.models.reforges;

import gg.sbs.api.data.sql.SqlJsonConverter;
import gg.sbs.api.data.sql.SqlModel;
import gg.sbs.api.data.sql.models.accessoryfamilies.AccessoryFamilyModel;
import gg.sbs.api.data.sql.models.itemtypes.ItemTypeModel;
import gg.sbs.api.data.sql.models.rarities.RarityModel;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Ref;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "reforges")
public class ReforgeModel implements SqlModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "item_type", nullable = false)
    private ItemTypeModel itemType;

    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @ManyToOne
    @JoinColumn(name = "rarity", nullable = false)
    private RarityModel rarity;

    @Column(name = "is_stone", nullable = false)
    private boolean isStone;

    @Column(name = "effects")
    @Convert(converter = SqlJsonConverter.class)
    private Map<String, Object> effects;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public long getId() {
        return id;
    }

    public ItemTypeModel getItemType() {
        return itemType;
    }

    public void setItemType(ItemTypeModel itemType) {
        this.itemType = itemType;
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

    public boolean isStone() {
        return isStone;
    }

    public void setStone(boolean stone) {
        isStone = stone;
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
        if (!(o instanceof ReforgeModel)) return false;
        return id == ((ReforgeModel) o).id;
    }
}
