package dev.sbs.api.data.model.skyblock.items;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.rarities.RaritySqlModel;
import dev.sbs.api.data.sql.converter.DoubleMapConverter;
import dev.sbs.api.data.sql.converter.IntegerListMapConverter;
import dev.sbs.api.data.sql.converter.IntegerMapConverter;
import dev.sbs.api.data.sql.converter.ObjectMapConverter;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Entity
@Table(
        name = "skyblock_items",
        indexes = {
                @Index(
                        columnList = "generator"
                ),
                @Index(
                        columnList = "rarity_key"
                )
        }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ItemSqlModel implements ItemModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
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
    @JoinColumn(name = "rarity_key")
    private RaritySqlModel rarity;

    @Getter
    @Setter
    @Id
    @Column(name = "item_id", length = 127, unique = true)
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
        if (!(o instanceof ItemSqlModel)) return false;
        ItemSqlModel that = (ItemSqlModel) o;

        return new EqualsBuilder()
                .append(this.getId(), that.getId())
                .append(this.getDurability(), that.getDurability())
                .append(this.getGeneratorTier(), that.getGeneratorTier())
                .append(this.isGlowing(), that.isGlowing())
                .append(this.getNpcSellPrice(), that.getNpcSellPrice())
                .append(this.isUnstackable(), that.isUnstackable())
                .append(this.isDungeonItem(), that.isDungeonItem())
                .append(this.getGearScore(), that.getGearScore())
                .append(this.getAbilityDamageScaling(), that.getAbilityDamageScaling())
                .append(this.getName(), that.getName())
                .append(this.getMaterial(), that.getMaterial())
                .append(this.getSkin(), that.getSkin())
                .append(this.getFurniture(), that.getFurniture())
                .append(this.getRarity(), that.getRarity())
                .append(this.getItemId(), that.getItemId())
                .append(this.getGenerator(), that.getGenerator())
                .append(this.getCategory(), that.getCategory())
                .append(this.getStats(), that.getStats())
                .append(this.getColor(), that.getColor())
                .append(this.getTieredStats(), that.getTieredStats())
                .append(this.getRequirements(), that.getRequirements())
                .append(this.getCatacombsRequirements(), that.getCatacombsRequirements())
                .append(this.getEssence(), that.getEssence())
                .append(this.getDescription(), that.getDescription())
                .append(this.getEnchantments(), that.getEnchantments())
                .append(this.getCrystal(), that.getCrystal())
                .append(this.getPrivateIsland(), that.getPrivateIsland())
                .append(this.getUpdatedAt(), that.getUpdatedAt())
                .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getId())
                .append(getName())
                .append(getMaterial())
                .append(getDurability())
                .append(getSkin())
                .append(getFurniture())
                .append(getRarity())
                .append(getItemId())
                .append(getGenerator())
                .append(getGeneratorTier())
                .append(isGlowing())
                .append(getCategory())
                .append(getStats())
                .append(getNpcSellPrice())
                .append(isUnstackable())
                .append(isDungeonItem())
                .append(getColor())
                .append(getTieredStats())
                .append(getGearScore())
                .append(getRequirements())
                .append(getCatacombsRequirements())
                .append(getEssence())
                .append(getDescription())
                .append(getAbilityDamageScaling())
                .append(getEnchantments())
                .append(getCrystal())
                .append(getPrivateIsland())
                .append(getUpdatedAt())
                .build();
    }
}
