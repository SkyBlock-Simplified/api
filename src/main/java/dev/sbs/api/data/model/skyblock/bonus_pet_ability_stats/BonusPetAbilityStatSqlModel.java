package dev.sbs.api.data.model.skyblock.bonus_pet_ability_stats;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.pet_abilities.PetAbilitySqlModel;
import dev.sbs.api.data.sql.converter.map.StringDoubleMapConverter;
import dev.sbs.api.data.sql.converter.map.StringObjectMapConverter;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(
    name = "skyblock_bonus_pet_ability_stats"
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BonusPetAbilityStatSqlModel implements BonusPetAbilityStatModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Getter
    @Setter
    @Id
    @ManyToOne
    @JoinColumn(name = "ability_key", nullable = false)
    private PetAbilitySqlModel petAbility;

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
        if (!(o instanceof BonusPetAbilityStatSqlModel)) return false;
        BonusPetAbilityStatSqlModel that = (BonusPetAbilityStatSqlModel) o;

        return new EqualsBuilder().append(this.getId(), that.getId())
            .append(this.getPetAbility(), that.getPetAbility())
            .append(this.getEffects(), that.getEffects())
            .append(this.getBuffEffects(), that.getBuffEffects())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.getId()).append(this.getPetAbility()).append(this.getEffects()).append(this.getBuffEffects()).append(this.getUpdatedAt()).build();
    }

}
