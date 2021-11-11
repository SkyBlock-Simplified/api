package dev.sbs.api.data.model.items;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.rarities.RaritySqlModel;
import dev.sbs.api.data.sql.converter.DoubleMapConverter;
import dev.sbs.api.data.sql.converter.IntegerListMapConverter;
import dev.sbs.api.data.sql.converter.IntegerMapConverter;
import dev.sbs.api.data.sql.converter.ObjectMapConverter;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "items")
public class ItemSqlModel implements ItemModel, SqlModel {

    @Getter
    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @ManyToOne(fetch = FetchType.LAZY)
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
    @SuppressWarnings("all")
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
