package gg.sbs.api.data.sql.models.pets;

import gg.sbs.api.data.sql.SqlJsonConverter;
import gg.sbs.api.data.sql.SqlModel;
import gg.sbs.api.data.sql.models.rarities.RarityModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "pets")
public class PetModel implements SqlModel {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "rarity", nullable = false)
    private RarityModel rarity;

    @Getter
    @Setter
    @Column(name = "effects_base")
    @Convert(converter = SqlJsonConverter.class)
    private Map<String, Object> effectsBase;

    @Getter
    @Setter
    @Column(name = "effects_per_level")
    @Convert(converter = SqlJsonConverter.class)
    private Map<String, Object> effectsPerLevel;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PetModel)) return false;

        PetModel petModel = (PetModel) o;

        if (id != petModel.id) return false;
        if (!name.equals(petModel.name)) return false;
        if (!rarity.equals(petModel.rarity)) return false;
        if (!effectsBase.equals(petModel.effectsBase)) return false;
        if (!effectsPerLevel.equals(petModel.effectsPerLevel)) return false;
        return updatedAt.equals(petModel.updatedAt);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + name.hashCode();
        result = 31 * result + rarity.hashCode();
        result = 31 * result + effectsBase.hashCode();
        result = 31 * result + effectsPerLevel.hashCode();
        result = 31 * result + updatedAt.hashCode();
        return result;
    }
}
