package dev.sbs.api.data.model.skyblock.pet_abilities;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.pets.PetSqlModel;
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
        name = "skyblock_pet_abilities",
        indexes = {
                @Index(
                        columnList = "pet_key, key",
                        unique = true
                ),
                @Index(
                        columnList = "pet_key, ordinal",
                        unique = true
                )
        }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PetAbilitySqlModel implements PetAbilityModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

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
    @ManyToOne
    @JoinColumn(name = "pet_key", nullable = false)
    private PetSqlModel pet;

    @Getter
    @Setter
    @Column(name = "ordinal", nullable = false)
    private Integer ordinal;

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