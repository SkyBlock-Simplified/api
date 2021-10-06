package gg.sbs.api.data.sql.models.skills;

import gg.sbs.api.data.sql.SqlModel;
import gg.sbs.api.util.builder.HashCodeBuilder;
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
    @Column(name = "skill_key", nullable = false, length = 127)
    private String skillKey;

    @Getter
    @Setter
    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @Getter
    @Setter
    @Column(name = "description", nullable = false, length = 127)
    private String description;

    @Getter
    @Setter
    @Column(name = "max_level", nullable = false)
    private int maxLevel;

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
        if (maxLevel != that.maxLevel) return false;
        if (!skillKey.equals(that.skillKey)) return false;
        if (!name.equals(that.name)) return false;
        if (!description.equals(that.description)) return false;
        return updatedAt.equals(that.updatedAt);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.id).append(this.skillKey).append(this.name).append(this.description).append(this.maxLevel).append(this.updatedAt).build();
    }

}