package gg.sbs.api.data.sql.models.items;

import gg.sbs.api.data.sql.SqlModel;
import gg.sbs.api.data.sql.converters.*;
import gg.sbs.api.data.sql.models.rarities.RarityModel;
import gg.sbs.api.util.builder.HashCodeBuilder;
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
    @ManyToOne
    @JoinColumn(name = "rarity")
    private RarityModel rarity;

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
    @Convert(converter = IntegerListMapConverter.class)
    private Map<String, List<Integer>> tieredStats;

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
    @Convert(converter = ObjectMapConverter.class)
    private Map<String, Object> essence;

    @Getter
    @Setter
    @Column(name = "description", length = 127)
    private String description;

    @Getter
    @Setter
    @Column(name = "ability_damage_scaling")
    private double abilityDamageScaling;

    @Getter
    @Setter
    @Column(name = "enchantments")
    @Convert(converter = IntegerMapConverter.class)
    private Map<String, Integer> enchantments;

    @Getter
    @Setter
    @Column(name = "crystal", length = 127)
    private String crystal;

    @Getter
    @Setter
    @Column(name = "private_island", length = 127)
    private String privateIsland;

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
        if (Double.compare(itemModel.abilityDamageScaling, abilityDamageScaling) != 0) return false;
        if (!name.equals(itemModel.name)) return false;
        if (!Objects.equals(material, itemModel.material)) return false;
        if (!Objects.equals(skin, itemModel.skin)) return false;
        if (!Objects.equals(furniture, itemModel.furniture)) return false;
        if (!Objects.equals(rarity, itemModel.rarity)) return false;
        if (!itemId.equals(itemModel.itemId)) return false;
        if (!Objects.equals(generator, itemModel.generator)) return false;
        if (!Objects.equals(category, itemModel.category)) return false;
        if (!Objects.equals(stats, itemModel.stats)) return false;
        if (!Objects.equals(color, itemModel.color)) return false;
        if (!Objects.equals(tieredStats, itemModel.tieredStats)) return false;
        if (!Objects.equals(requirements, itemModel.requirements)) return false;
        if (!Objects.equals(catacombsRequirements, itemModel.catacombsRequirements)) return false;
        if (!Objects.equals(essence, itemModel.essence)) return false;
        if (!Objects.equals(description, itemModel.description)) return false;
        if (!Objects.equals(enchantments, itemModel.enchantments)) return false;
        if (!Objects.equals(crystal, itemModel.crystal)) return false;
        if (!Objects.equals(privateIsland, itemModel.privateIsland)) return false;
        return updatedAt.equals(itemModel.updatedAt);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.id).append(this.name).append(this.material).append(this.durability).append(this.skin).append(this.furniture).append(this.rarity)
                .append(this.itemId).append(this.generator).append(this.generatorTier).append(this.glowing).append(this.category).append(this.stats).append(this.npcSellPrice)
                .append(this.unstackable).append(this.dungeonItem).append(this.color).append(this.tieredStats).append(this.gearScore).append(this.requirements)
                .append(this.catacombsRequirements).append(this.essence).append(this.description).append(this.abilityDamageScaling).append(this.enchantments).append(this.crystal)
                .append(this.privateIsland).append(this.updatedAt).build();
    }

}