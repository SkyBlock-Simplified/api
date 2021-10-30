package dev.sbs.api.model.sql.pets.petstats;

import dev.sbs.api.data.sql.converter.IntegerListConverter;
import dev.sbs.api.data.sql.model.SqlModel;
import dev.sbs.api.model.PetStatModel;
import dev.sbs.api.model.sql.pets.PetSqlModel;
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
@Table(name = "pet_stats")
public class PetStatSqlModel implements PetStatModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "pet_key", nullable = false)
    private PetSqlModel pet;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "stat_key", nullable = false)
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
