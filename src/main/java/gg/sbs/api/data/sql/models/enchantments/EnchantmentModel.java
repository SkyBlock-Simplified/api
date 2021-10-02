package gg.sbs.api.data.sql.models.enchantments;

import gg.sbs.api.data.sql.SqlJsonConverter;
import gg.sbs.api.data.sql.SqlModel;
import gg.sbs.api.data.sql.models.SqlEffectsModel;
import gg.sbs.api.data.sql.models.itemtypes.ItemTypeModel;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "enchantments")
public class EnchantmentModel extends SqlEffectsModel implements SqlModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "item_type", nullable = false)
    private ItemTypeModel itemType;

    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @Column(name = "item_level", nullable = false)
    private int itemLevel;

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

    public int getItemLevel() {
        return itemLevel;
    }

    public void setItemLevel(int itemLevel) {
        this.itemLevel = itemLevel;
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
        if (!(o instanceof EnchantmentModel)) return false;
        return id == ((EnchantmentModel) o).id;
    }

    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (itemType != null ? itemType.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + itemLevel;
        result = 31 * result + (effects != null ? effects.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        return result;
    }
}
