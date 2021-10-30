package dev.sbs.api.model.sql.skills.skilllevels;

import dev.sbs.api.data.sql.converter.StringListConverter;
import dev.sbs.api.data.sql.model.SqlModel;
import dev.sbs.api.model.SkillLevelModel;
import dev.sbs.api.model.sql.skills.SkillSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "skill_levels")
public class SkillLevelSqlModel implements SkillLevelModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "skill_key", nullable = false, referencedColumnName = "key")
    private SkillSqlModel skill;

    @Getter
    @Setter
    @Column(name = "level", nullable = false)
    private int level;

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
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
        /*if (this == o) return true;
        if (!(o instanceof SkillLevelSqlModel)) return false;

        SkillLevelSqlModel that = (SkillLevelSqlModel) o;

        if (id != that.id) return false;
        if (level != that.level) return false;
        if (Double.compare(that.totalExpRequired, totalExpRequired) != 0) return false;
        if (!skill.equals(that.skill)) return false;
        if (!unlocks.equals(that.unlocks)) return false;
        return updatedAt.equals(that.updatedAt);*/
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
