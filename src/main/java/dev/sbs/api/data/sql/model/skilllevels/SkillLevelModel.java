package dev.sbs.api.data.sql.model.skilllevels;

import dev.sbs.api.data.sql.converter.StringListConverter;
import dev.sbs.api.data.sql.model.SqlModel;
import dev.sbs.api.data.sql.model.skills.SkillModel;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "skill_levels")
public class SkillLevelModel implements SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "skill", nullable = false)
    private SkillModel skill;

    @Getter
    @Setter
    @Column(name = "skill_level", nullable = false)
    private int skillLevel;

    @Getter
    @Setter
    @Column(name = "total_exp_required", nullable = false)
    private double totalExpRequired;

    @Getter
    @Setter
    @Column(name = "unlocks", nullable = false)
    @Convert(converter = StringListConverter.class)
    private List<String> unlocks;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SkillLevelModel)) return false;

        SkillLevelModel that = (SkillLevelModel) o;

        if (id != that.id) return false;
        if (skillLevel != that.skillLevel) return false;
        if (Double.compare(that.totalExpRequired, totalExpRequired) != 0) return false;
        if (!skill.equals(that.skill)) return false;
        if (!unlocks.equals(that.unlocks)) return false;
        return updatedAt.equals(that.updatedAt);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
