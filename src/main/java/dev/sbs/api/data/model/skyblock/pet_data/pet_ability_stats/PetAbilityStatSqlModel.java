package dev.sbs.api.data.model.skyblock.pet_data.pet_ability_stats;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.pet_data.pet_abilities.PetAbilitySqlModel;
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
    name = "skyblock_pet_ability_stats",
    indexes = {
        @Index(
            columnList = "ability_key"
        ),
        @Index(
            columnList = "stat_key"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PetAbilityStatSqlModel implements PetAbilityStatModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "ability_key", nullable = false)
    private PetAbilitySqlModel ability;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "stat_key")
    private StatSqlModel stat;

    @Getter
    @Setter
    @Column(name = "rarities", nullable = false)
    @Convert(converter = IntegerListConverter.class)
    private List<Integer> rarities;

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
    @Column(name = "round", nullable = false)
    private boolean roundingNeeded;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PetAbilityStatSqlModel that = (PetAbilityStatSqlModel) o;

        return new EqualsBuilder()
            .append(this.isRoundingNeeded(), that.isRoundingNeeded())
            .append(this.getId(), that.getId())
            .append(this.getAbility(), that.getAbility())
            .append(this.getStat(), that.getStat())
            .append(this.getRarities(), that.getRarities())
            .append(this.getBaseValue(), that.getBaseValue())
            .append(this.getLevelBonus(), that.getLevelBonus())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getAbility())
            .append(this.getStat())
            .append(this.getRarities())
            .append(this.getBaseValue())
            .append(this.getLevelBonus())
            .append(this.isRoundingNeeded())
            .append(this.getUpdatedAt())
            .build();
    }

}
