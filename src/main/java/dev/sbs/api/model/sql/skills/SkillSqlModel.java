package dev.sbs.api.model.sql.skills;

import dev.sbs.api.data.sql.model.SqlModel;
import dev.sbs.api.model.SkillModel;
import dev.sbs.api.model.sql.items.ItemSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import dev.sbs.api.util.helper.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "skills")
public class SkillSqlModel implements SkillModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @Column(name = "key", nullable = false, length = 127)
    private String key;

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
    @Setter
    @Column(name = "item_id", length = 127)
    private ItemSqlModel item;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // TODO: Load Exp Table

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
        /*if (this == o) return true;
        if (!(o instanceof SkillSqlModel)) return false;

        SkillSqlModel that = (SkillSqlModel) o;

        if (id != that.id) return false;
        if (maxLevel != that.maxLevel) return false;
        if (!StringUtil.equals(key, that.key)) return false;
        if (!StringUtil.equals(name, that.name)) return false;
        if (!description.equals(that.description)) return false;
        return updatedAt.equals(that.updatedAt);*/
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
