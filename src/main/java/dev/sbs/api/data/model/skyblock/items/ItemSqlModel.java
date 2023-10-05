package dev.sbs.api.data.model.skyblock.items;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.item_types.ItemTypeSqlModel;
import dev.sbs.api.data.model.skyblock.rarities.RaritySqlModel;
import dev.sbs.api.data.sql.converter.list.StringObjectMapListConverter;
import dev.sbs.api.data.sql.converter.list.StringObjectMapListListConverter;
import dev.sbs.api.data.sql.converter.map.StringDoubleListMapConverter;
import dev.sbs.api.data.sql.converter.map.StringDoubleMapConverter;
import dev.sbs.api.data.sql.converter.map.StringObjectMapConverter;
import dev.sbs.api.util.builder.hash.EqualsBuilder;
import dev.sbs.api.util.builder.hash.HashCodeBuilder;
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
        ),
        @Index(
            columnList = "item_type_key"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ItemSqlModel implements ItemModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Getter
    @Setter
    @Id
    @Column(name = "item_id", length = 256, unique = true)
    private String itemId;

    @Getter
    @Setter
    @Column(name = "name", nullable = false, length = 256)
    private String name;

    @Getter
    @Setter
    @Column(name = "material", length = 256)
    private String material;

    @Getter
    @Setter
    @Column(name = "durability", nullable = false)
    private Integer durability;

    @Getter
    @Setter
    @Column(name = "description", length = 256)
    private String description;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "rarity_key", nullable = false)
    private RaritySqlModel rarity;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "item_type_key")
    private ItemTypeSqlModel type;

    @Getter
    @Setter
    @Column(name = "color", length = 31)
    private String color;

    @Getter
    @Setter
    @Column(name = "obtainable", nullable = false)
    private boolean obtainable;

    @Getter
    @Setter
    @Column(name = "glowing", nullable = false)
    private boolean glowing;

    @Getter
    @Setter
    @Column(name = "unstackable", nullable = false)
    private boolean unstackable;

    @Getter
    @Setter
    @Column(name = "special_museum", nullable = false)
    private boolean inSpecialMuseum;

    @Getter
    @Setter
    @Column(name = "dungeon_item", nullable = false)
    private boolean dungeonItem;

    @Getter
    @Setter
    @Column(name = "attributable", nullable = false)
    private boolean attributable;

    @Getter
    @Setter
    @Column(name = "hidden_from_viewrecipe", nullable = false)
    private boolean hiddenFromViewrecipe;

    @Getter
    @Setter
    @Column(name = "salvageable_from_recipe", nullable = false)
    private boolean salvageableFromRecipe;

    @Getter
    @Setter
    @Column(name = "not_reforgeable", nullable = false)
    private boolean notReforgeable;

    @Getter
    @Setter
    @Column(name = "rift_transferrable", nullable = false)
    private boolean riftTransferrable;

    @Getter
    @Setter
    @Column(name = "rift_lose_motes_value_on_transfer", nullable = false)
    private boolean riftLoseMotesValueOnTransfer;

    @Getter
    @Setter
    @Column(name = "rift_motes_sell_price", nullable = false)
    private Double riftMotesSellPrice;

    @Getter
    @Setter
    @Column(name = "npc_sell_price", nullable = false)
    private Double npcSellPrice;

    @Getter
    @Setter
    @Column(name = "gear_score", nullable = false)
    private Integer gearScore;

    @Getter
    @Setter
    @Column(name = "generator", length = 256)
    private String generator;

    @Getter
    @Setter
    @Column(name = "generator_tier", nullable = false)
    private Integer generatorTier;

    @Getter
    @Setter
    @Column(name = "ability_damage_scaling", nullable = false)
    private Double abilityDamageScaling;

    @Getter
    @Setter
    @Column(name = "origin", length = 10)
    private String origin;

    @Getter
    @Setter
    @Column(name = "soulbound", length = 10)
    private String soulbound;

    @Getter
    @Setter
    @Column(name = "furniture", length = 256)
    private String furniture;

    @Getter
    @Setter
    @Column(name = "sword_type", length = 256)
    private String swordType;

    @Getter
    @Setter
    @Column(name = "skin", length = 1023)
    private String skin;

    @Getter
    @Setter
    @Column(name = "crystal", length = 256)
    private String crystal;

    @Getter
    @Setter
    @Column(name = "private_island", length = 256)
    private String privateIsland;

    @Getter
    @Setter
    @Column(name = "stats", nullable = false)
    @Convert(converter = StringDoubleMapConverter.class)
    private Map<String, Double> stats;

    @Getter
    @Setter
    @Column(name = "tiered_stats", nullable = false)
    @Convert(converter = StringDoubleListMapConverter.class)
    private Map<String, List<Double>> tieredStats;

    @Getter
    @Setter
    @Column(name = "requirements", nullable = false)
    @Convert(converter = StringObjectMapListConverter.class)
    private List<Map<String, Object>> requirements;

    @Getter
    @Setter
    @Column(name = "catacombs_requirements", nullable = false)
    @Convert(converter = StringObjectMapListConverter.class)
    private List<Map<String, Object>> catacombsRequirements;

    @Getter
    @Setter
    @Column(name = "upgrade_costs", nullable = false)
    @Convert(converter = StringObjectMapListListConverter.class)
    private List<List<Map<String, Object>>> upgradeCosts;

    @Getter
    @Setter
    @Column(name = "gemstone_slots", nullable = false)
    @Convert(converter = StringObjectMapListConverter.class)
    private List<Map<String, Object>> gemstoneSlots;

    @Getter
    @Setter
    @Column(name = "dungeon_item_conversion_cost", nullable = false)
    @Convert(converter = StringObjectMapConverter.class)
    private Map<String, Object> dungeonItemConversionCost;

    @Getter
    @Setter
    @Column(name = "enchantments", nullable = false)
    @Convert(converter = StringDoubleMapConverter.class)
    private Map<String, Double> enchantments;

    @Getter
    @Setter
    @Column(name = "prestige", nullable = false)
    @Convert(converter = StringObjectMapConverter.class)
    private Map<String, Object> prestige;

    @Getter
    @Setter
    @Column(name = "salvages", nullable = false)
    @Convert(converter = StringObjectMapListConverter.class)
    private List<Map<String, Object>> salvages;

    @Getter
    @Setter
    @Column(name = "item_specific", nullable = false)
    @Convert(converter = StringObjectMapConverter.class)
    private Map<String, Object> itemSpecific;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemSqlModel that = (ItemSqlModel) o;

        return new EqualsBuilder()
            .append(this.isObtainable(), that.isObtainable())
            .append(this.isGlowing(), that.isGlowing())
            .append(this.isUnstackable(), that.isUnstackable())
            .append(this.isInSpecialMuseum(), that.isInSpecialMuseum())
            .append(this.isDungeonItem(), that.isDungeonItem())
            .append(this.isAttributable(), that.isAttributable())
            .append(this.isHiddenFromViewrecipe(), that.isHiddenFromViewrecipe())
            .append(this.isSalvageableFromRecipe(), that.isSalvageableFromRecipe())
            .append(this.isNotReforgeable(), that.isNotReforgeable())
            .append(this.isRiftTransferrable(), that.isRiftTransferrable())
            .append(this.isRiftLoseMotesValueOnTransfer(), that.isRiftLoseMotesValueOnTransfer())
            .append(this.getId(), that.getId())
            .append(this.getItemId(), that.getItemId())
            .append(this.getName(), that.getName())
            .append(this.getMaterial(), that.getMaterial())
            .append(this.getDurability(), that.getDurability())
            .append(this.getDescription(), that.getDescription())
            .append(this.getRarity(), that.getRarity())
            .append(this.getType(), that.getType())
            .append(this.getColor(), that.getColor())
            .append(this.getRiftMotesSellPrice(), that.getRiftMotesSellPrice())
            .append(this.getNpcSellPrice(), that.getNpcSellPrice())
            .append(this.getGearScore(), that.getGearScore())
            .append(this.getGenerator(), that.getGenerator())
            .append(this.getGeneratorTier(), that.getGeneratorTier())
            .append(this.getAbilityDamageScaling(), that.getAbilityDamageScaling())
            .append(this.getOrigin(), that.getOrigin())
            .append(this.getSoulbound(), that.getSoulbound())
            .append(this.getFurniture(), that.getFurniture())
            .append(this.getSwordType(), that.getSwordType())
            .append(this.getSkin(), that.getSkin())
            .append(this.getCrystal(), that.getCrystal())
            .append(this.getPrivateIsland(), that.getPrivateIsland())
            .append(this.getStats(), that.getStats())
            .append(this.getTieredStats(), that.getTieredStats())
            .append(this.getRequirements(), that.getRequirements())
            .append(this.getCatacombsRequirements(), that.getCatacombsRequirements())
            .append(this.getUpgradeCosts(), that.getUpgradeCosts())
            .append(this.getGemstoneSlots(), that.getGemstoneSlots())
            .append(this.getDungeonItemConversionCost(), that.getDungeonItemConversionCost())
            .append(this.getEnchantments(), that.getEnchantments())
            .append(this.getPrestige(), that.getPrestige())
            .append(this.getSalvages(), that.getSalvages())
            .append(this.getItemSpecific(), that.getItemSpecific())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getItemId())
            .append(this.getName())
            .append(this.getMaterial())
            .append(this.getDurability())
            .append(this.getDescription())
            .append(this.getRarity())
            .append(this.getType())
            .append(this.getColor())
            .append(this.isObtainable())
            .append(this.isGlowing())
            .append(this.isUnstackable())
            .append(this.isInSpecialMuseum())
            .append(this.isDungeonItem())
            .append(this.isAttributable())
            .append(this.isHiddenFromViewrecipe())
            .append(this.isSalvageableFromRecipe())
            .append(this.isNotReforgeable())
            .append(this.isRiftTransferrable())
            .append(this.isRiftLoseMotesValueOnTransfer())
            .append(this.getRiftMotesSellPrice())
            .append(this.getNpcSellPrice())
            .append(this.getGearScore())
            .append(this.getGenerator())
            .append(this.getGeneratorTier())
            .append(this.getAbilityDamageScaling())
            .append(this.getOrigin())
            .append(this.getSoulbound())
            .append(this.getFurniture())
            .append(this.getSwordType())
            .append(this.getSkin())
            .append(this.getCrystal())
            .append(this.getPrivateIsland())
            .append(this.getStats())
            .append(this.getTieredStats())
            .append(this.getRequirements())
            .append(this.getCatacombsRequirements())
            .append(this.getUpgradeCosts())
            .append(this.getGemstoneSlots())
            .append(this.getDungeonItemConversionCost())
            .append(this.getEnchantments())
            .append(this.getPrestige())
            .append(this.getSalvages())
            .append(this.getItemSpecific())
            .append(this.getUpdatedAt())
            .build();
    }

}
