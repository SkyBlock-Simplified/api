package dev.sbs.api.data.model.skyblock.enchantment_data.enchantment_stats;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.enchantment_data.enchantments.EnchantmentSqlModel;
import dev.sbs.api.data.model.skyblock.stats.StatSqlModel;
import dev.sbs.api.data.sql.converter.list.IntegerListConverter;
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

@Entity
@Table(
    name = "skyblock_enchantment_stats",
    indexes = {
        @Index(
            columnList = "enchantment_key, stat_key, buff_key, levels",
            unique = true
        ),
        @Index(
            columnList = "stat_key"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EnchantmentStatSqlModel implements EnchantmentStatModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "enchantment_key", nullable = false)
    private EnchantmentSqlModel enchantment;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "stat_key")
    private StatSqlModel stat;

    @Getter
    @Setter
    @Column(name = "buff_key", length = 256)
    private String buffKey;

    @Getter
    @Setter
    @Column(name = "levels", nullable = false)
    @Convert(converter = IntegerListConverter.class)
    private List<Integer> levels;

    @Getter
    @Setter
    @Column(name = "base_value", nullable = false)
    private Double baseValue;

    @Getter
    @Setter
    @Column(name = "level_bonus", nullable = false)
    private Double levelBonus;

    @Getter
    @Setter
    @Column(name = "percentage", nullable = false)
    private boolean percentage;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EnchantmentStatSqlModel that = (EnchantmentStatSqlModel) o;

        return new EqualsBuilder()
            .append(this.isPercentage(), that.isPercentage())
            .append(this.getId(), that.getId())
            .append(this.getEnchantment(), that.getEnchantment())
            .append(this.getStat(), that.getStat())
            .append(this.getBuffKey(), that.getBuffKey())
            .append(this.getLevels(), that.getLevels())
            .append(this.getBaseValue(), that.getBaseValue())
            .append(this.getLevelBonus(), that.getLevelBonus())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getEnchantment())
            .append(this.getStat())
            .append(this.getBuffKey())
            .append(this.getLevels())
            .append(this.getBaseValue())
            .append(this.getLevelBonus())
            .append(this.isPercentage())
            .append(this.getUpdatedAt())
            .build();
    }

}
