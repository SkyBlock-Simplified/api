package dev.sbs.api.model.sql.collections;

import dev.sbs.api.data.sql.model.SqlModel;
import dev.sbs.api.model.CollectionModel;
import dev.sbs.api.model.sql.skills.SkillSqlModel;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "collections")
public class CollectionSqlModel implements CollectionModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "skill_key", nullable = false)
    private SkillSqlModel skill;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CollectionSqlModel)) return false;

        CollectionSqlModel that = (CollectionSqlModel) o;

        if (id != that.id) return false;
        if (!skill.equals(that.skill)) return false;
        return updatedAt.equals(that.updatedAt);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
