package dev.sbs.api.data.model.pet_stats;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.pets.PetSqlModel;
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
        name = "pet_stats",
        indexes = {
                @Index(
                        columnList = "pet_key, stat_key",
                        unique = true
                ),
                @Index(
                        columnList = "pet_key, ordinal",
                        unique = true
                ),
                @Index(
                        columnList = "pet_key"
                ),
                @Index(
                        columnList = "stat_key"
                )
        }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    @JoinColumn(name = "stat_key")
    private StatSqlModel stat;

    @Getter
    @Setter
    @Column(name = "ordinal", nullable = false)
    private int ordinal;

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
    @Column(name = "level_bonus", nullable = false)
    private double levelBonus;

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
