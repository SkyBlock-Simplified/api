package gg.sbs.api.data.sql.models.items;

import gg.sbs.api.data.sql.SqlModel;
import gg.sbs.api.data.sql.converters.DoubleListConverter;
import gg.sbs.api.data.sql.converters.DoubleMapConverter;
import gg.sbs.api.data.sql.converters.ObjectMapConverter;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "items")
public class ItemModel implements SqlModel {
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
    @Column(name = "material", length = 127)
    private String material;

    @Getter
    @Setter
    @Column(name = "durability")
    private int durability;

    @Getter
    @Setter
    @Column(name = "skin", length = 1023)
    private String skin;

    @Getter
    @Setter
    @Column(name = "furniture", length = 127)
    private String furniture;

    @Getter
    @Setter
    @Column(name = "tier", length = 15)
    private String tier;

    @Getter
    @Setter
    @Column(name = "item_id", length = 127)
    private String itemId;

    @Getter
    @Setter
    @Column(name = "generator", length = 127)
    private String generator;

    @Getter
    @Setter
    @Column(name = "generator_tier")
    private int generatorTier;

    @Getter
    @Setter
    @Column(name = "glowing")
    private boolean glowing;

    @Getter
    @Setter
    @Column(name = "category", length = 31)
    private String category;

    @Getter
    @Setter
    @Column(name = "stats")
    @Convert(converter = DoubleMapConverter.class)
    private Map<String, Double> stats;

    @Getter
    @Setter
    @Column(name = "npc_sell_price")
    private double npcSellPrice;

    @Getter
    @Setter
    @Column(name = "unstackable")
    private boolean unstackable;

    @Getter
    @Setter
    @Column(name = "dungeon_item")
    private boolean dungeonItem;

    @Getter
    @Setter
    @Column(name = "color", length = 31)
    private String color;

    @Getter
    @Setter
    @Column(name = "tiered_stats")
    @Convert(converter = DoubleListConverter.class)
    private List<Double> tieredStats;

    @Getter
    @Setter
    @Column(name = "gear_score")
    private int gearScore;

    @Getter
    @Setter
    @Column(name = "requirements")
    @Convert(converter = ObjectMapConverter.class)
    private Map<String, Object> requirements;

    @Getter
    @Setter
    @Column(name = "catacombs_requirements")
    @Convert(converter = ObjectMapConverter.class)
    private Map<String, Object> catacombsRequirements;

    @Getter
    @Setter
    @Column(name = "essence")
    @Convert(converter = DoubleListConverter.class)
    private List<Double> essence;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemModel)) return false;

        ItemModel itemModel = (ItemModel) o;

        if (id != itemModel.id) return false;
        if (durability != itemModel.durability) return false;
        if (generatorTier != itemModel.generatorTier) return false;
        if (glowing != itemModel.glowing) return false;
        if (Double.compare(itemModel.npcSellPrice, npcSellPrice) != 0) return false;
        if (unstackable != itemModel.unstackable) return false;
        if (dungeonItem != itemModel.dungeonItem) return false;
        if (gearScore != itemModel.gearScore) return false;
        if (!name.equals(itemModel.name)) return false;
        if (material != null ? !material.equals(itemModel.material) : itemModel.material != null) return false;
        if (skin != null ? !skin.equals(itemModel.skin) : itemModel.skin != null) return false;
        if (furniture != null ? !furniture.equals(itemModel.furniture) : itemModel.furniture != null) return false;
        if (tier != null ? !tier.equals(itemModel.tier) : itemModel.tier != null) return false;
        if (!itemId.equals(itemModel.itemId)) return false;
        if (generator != null ? !generator.equals(itemModel.generator) : itemModel.generator != null) return false;
        if (category != null ? !category.equals(itemModel.category) : itemModel.category != null) return false;
        if (stats != null ? !stats.equals(itemModel.stats) : itemModel.stats != null) return false;
        if (color != null ? !color.equals(itemModel.color) : itemModel.color != null) return false;
        if (tieredStats != null ? !tieredStats.equals(itemModel.tieredStats) : itemModel.tieredStats != null)
            return false;
        if (requirements != null ? !requirements.equals(itemModel.requirements) : itemModel.requirements != null)
            return false;
        if (catacombsRequirements != null ? !catacombsRequirements.equals(itemModel.catacombsRequirements) : itemModel.catacombsRequirements != null)
            return false;
        if (essence != null ? !essence.equals(itemModel.essence) : itemModel.essence != null) return false;
        return updatedAt.equals(itemModel.updatedAt);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + name.hashCode();
        result = 31 * result + (material != null ? material.hashCode() : 0);
        result = 31 * result + durability;
        result = 31 * result + (skin != null ? skin.hashCode() : 0);
        result = 31 * result + (furniture != null ? furniture.hashCode() : 0);
        result = 31 * result + (tier != null ? tier.hashCode() : 0);
        result = 31 * result + itemId.hashCode();
        result = 31 * result + (generator != null ? generator.hashCode() : 0);
        result = 31 * result + generatorTier;
        result = 31 * result + (glowing ? 1 : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (stats != null ? stats.hashCode() : 0);
        temp = Double.doubleToLongBits(npcSellPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (unstackable ? 1 : 0);
        result = 31 * result + (dungeonItem ? 1 : 0);
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + (tieredStats != null ? tieredStats.hashCode() : 0);
        result = 31 * result + gearScore;
        result = 31 * result + (requirements != null ? requirements.hashCode() : 0);
        result = 31 * result + (catacombsRequirements != null ? catacombsRequirements.hashCode() : 0);
        result = 31 * result + (essence != null ? essence.hashCode() : 0);
        result = 31 * result + updatedAt.hashCode();
        return result;
    }
}