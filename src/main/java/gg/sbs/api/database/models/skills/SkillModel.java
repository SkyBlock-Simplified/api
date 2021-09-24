package gg.sbs.api.database.models.skills;

import gg.sbs.api.database.SqlModel;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "skills")
public class SkillModel implements SqlModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @Column(name = "has_collection", nullable = false)
    private boolean hasCollection;

    @Column(name = "cosmetic", nullable = false)
    private boolean cosmetic;

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

    public boolean isHasCollection() {
        return hasCollection;
    }

    public void setHasCollection(boolean hasCollection) {
        this.hasCollection = hasCollection;
    }

    public boolean isCosmetic() {
        return cosmetic;
    }

    public void setCosmetic(boolean cosmetic) {
        this.cosmetic = cosmetic;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
