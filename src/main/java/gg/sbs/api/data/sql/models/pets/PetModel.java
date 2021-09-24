package gg.sbs.api.data.sql.models.pets;

import gg.sbs.api.data.sql.SqlJsonConverter;
import gg.sbs.api.data.sql.SqlModel;
import gg.sbs.api.data.sql.models.rarities.RarityModel;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "pets")
public class PetModel implements SqlModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @ManyToOne
    @JoinColumn(name = "rarity", nullable = false)
    private RarityModel rarity;

    @Column(name = "effects_base")
    @Convert(converter = SqlJsonConverter.class)
    private Map<String, Object> effectsBase;

    @Column(name = "effects_per_level")
    @Convert(converter = SqlJsonConverter.class)
    private Map<String, Object> effectsPerLevel;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RarityModel getRarity() {
        return rarity;
    }

    public void setRarity(RarityModel rarity) {
        this.rarity = rarity;
    }

    public Map<String, Object> getEffectsBase() {
        return effectsBase;
    }

    public void setEffectsBase(Map<String, Object> effectsBase) {
        this.effectsBase = effectsBase;
    }

    public Map<String, Object> getEffectsPerLevel() {
        return effectsPerLevel;
    }

    public void setEffectsPerLevel(Map<String, Object> effectsPerLevel) {
        this.effectsPerLevel = effectsPerLevel;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
