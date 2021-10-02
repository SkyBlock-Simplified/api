package gg.sbs.api.data.sql.models.skills;

import gg.sbs.api.data.sql.SqlModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "skills")
public class SkillModel implements SqlModel {
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
    @Column(name = "has_collection", nullable = false)
    private boolean hasCollection;

    @Getter
    @Setter
    @Column(name = "cosmetic", nullable = false)
    private boolean cosmetic;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SkillModel)) return false;

        SkillModel that = (SkillModel) o;

        if (id != that.id) return false;
        if (hasCollection != that.hasCollection) return false;
        if (cosmetic != that.cosmetic) return false;
        if (!name.equals(that.name)) return false;
        return updatedAt.equals(that.updatedAt);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + name.hashCode();
        result = 31 * result + (hasCollection ? 1 : 0);
        result = 31 * result + (cosmetic ? 1 : 0);
        result = 31 * result + updatedAt.hashCode();
        return result;
    }
}
