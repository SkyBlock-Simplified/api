package dev.sbs.api.data.model.skyblock.enchantment_stats;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.enchantments.EnchantmentSqlModel;
import dev.sbs.api.data.model.skyblock.stats.StatSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(
    name = "skyblock_enchantment_stats",
    indexes = {
        @Index(
            columnList = "enchantment_key, stat_key, buff_key",
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
    @Column(name = "id", nullable = false, unique = true)
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
            .append(this.getBaseValue())
            .append(this.getLevelBonus())
            .append(this.isPercentage())
            .append(this.getUpdatedAt())
            .build();
    }

}
