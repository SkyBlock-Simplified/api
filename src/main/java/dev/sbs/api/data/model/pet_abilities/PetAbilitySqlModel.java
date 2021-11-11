package dev.sbs.api.data.model.pet_abilities;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.pets.PetSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.Instant;

@Entity
@Transactional
@Table(
        name = "pet_abilities",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "pet_ability",
                        columnNames = { "pet_key", "key" }
                ),
                @UniqueConstraint(
                        name = "pet_ability_ordinal",
                        columnNames = { "pet_key", "ordinal" }
                )
        }
)
public class PetAbilitySqlModel implements PetAbilityModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @Id
    @Column(name = "key", nullable = false, length = 127)
    private String key;

    @Getter
    @Setter
    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_key", nullable = false)
    private PetSqlModel pet;

    @Getter
    @Setter
    @Column(name = "ordinal", nullable = false)
    private int ordinal;

    @Getter
    @Setter
    @Column(name = "description", nullable = false, length = 127)
    private String description;

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
