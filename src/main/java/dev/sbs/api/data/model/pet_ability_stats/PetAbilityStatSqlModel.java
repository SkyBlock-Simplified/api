package dev.sbs.api.data.model.pet_ability_stats;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.pet_abilities.PetAbilitySqlModel;
import dev.sbs.api.data.model.stats.StatSqlModel;
import dev.sbs.api.data.sql.converter.IntegerListConverter;
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

@Entity
@Table(
        name = "pet_ability_stats",
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
    //@Convert(converter = RaritySqlModelListConverter.class)
    //private List<RaritySqlModel> rarities;
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
    @SuppressWarnings("all")
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
