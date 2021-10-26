package dev.sbs.api.model.sql.pets.petabilitystats;

import dev.sbs.api.data.sql.model.SqlModel;
import dev.sbs.api.model.PetAbilityStatModel;
import dev.sbs.api.model.sql.pets.petabilities.PetAbilitySqlModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

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
    @JoinColumn(name = "ability_key", nullable = false)
    private PetAbilitySqlModel ability;

    @Getter
    @Setter
    @Column(name = "priority", nullable = false)
    private int priority;

    @Getter
    @Setter
    @Column(name = "priority", nullable = false)
    //@Convert(converter = RaritySqlModelListConverter.class)
    //private List<RaritySqlModel> rarities;
    private List<Integer> rarities;

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

}
