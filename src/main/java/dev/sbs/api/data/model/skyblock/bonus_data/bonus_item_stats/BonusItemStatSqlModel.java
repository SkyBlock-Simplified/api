package dev.sbs.api.data.model.skyblock.bonus_data.bonus_item_stats;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.discord.optimizer_mob_types.OptimizerMobTypeSqlModel;
import dev.sbs.api.data.model.skyblock.items.ItemSqlModel;
import dev.sbs.api.data.sql.converter.map.StringDoubleMapConverter;
import dev.sbs.api.data.sql.converter.map.StringObjectMapConverter;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Map;

@Entity
@Table(
    name = "skyblock_bonus_item_stats",
    indexes = {
        @Index(
            columnList = "item_id, stats, reforges, gems, required_mob_type_key",
            unique = true
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BonusItemStatSqlModel implements BonusItemStatModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemSqlModel item;

    @Getter
    @Setter
    @Column(name = "stats", nullable = false)
    private boolean forStats;

    @Getter
    @Setter
    @Column(name = "reforges", nullable = false)
    private boolean forReforges;

    @Getter
    @Setter
    @Column(name = "gems", nullable = false)
    private boolean forGems;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "required_mob_type_key", referencedColumnName = "key")
    private OptimizerMobTypeSqlModel requiredMobType;

    @Getter
    @Setter
    @Column(name = "effects", nullable = false)
    @Convert(converter = StringDoubleMapConverter.class)
    private Map<String, Double> effects;

    @Getter
    @Setter
    @Column(name = "buff_effects", nullable = false)
    @Convert(converter = StringObjectMapConverter.class)
    private Map<String, Object> buffEffects;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BonusItemStatSqlModel that = (BonusItemStatSqlModel) o;

        return new EqualsBuilder()
            .append(this.isForStats(), that.isForStats())
            .append(this.isForReforges(), that.isForReforges())
            .append(this.isForGems(), that.isForGems())
            .append(this.getId(), that.getId())
            .append(this.getItem(), that.getItem())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getItem())
            .append(this.isForStats())
            .append(this.isForReforges())
            .append(this.isForGems())
            .append(this.getUpdatedAt())
            .build();
    }

}
