package dev.sbs.api.model.sql.pets.petabilitystats;

import dev.sbs.api.data.sql.converter.IntegerListConverter;
import dev.sbs.api.data.sql.model.SqlModel;
import dev.sbs.api.model.PetAbilityStatModel;
import dev.sbs.api.model.sql.pets.petabilities.PetAbilitySqlModel;
import dev.sbs.api.model.sql.stats.StatSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "pet_ability_stats")
public class PetAbilityStatSqlModel implements PetAbilityStatModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "ability_key", nullable = false, referencedColumnName = "key")
    private PetAbilitySqlModel ability;


    @Getter
    @Setter
    @Column(name = "rarities")
    //@Convert(converter = RaritySqlModelListConverter.class)
    //private List<RaritySqlModel> rarities;
    @Convert(converter = IntegerListConverter.class)
    private List<Integer> rarities;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "stat_key", referencedColumnName = "key")
    private StatSqlModel stat;

    @Getter
    @Setter
    @Column(name = "base_value", nullable = false)
    private double baseValue;

    @Getter
    @Setter
    @Column(name = "expression", nullable = false)
    private String expression;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
