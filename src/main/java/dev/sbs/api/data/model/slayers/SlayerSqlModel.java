package dev.sbs.api.data.model.slayers;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skill_levels.SkillLevelSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import dev.sbs.api.util.concurrent.ConcurrentList;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "slayers")
public class SlayerSqlModel implements SlayerModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @Id
    @Column(name = "key", nullable = false, length = 127, unique = true)
    private String key;

    @Getter
    @Setter
    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @Getter
    @Setter
    @Column(name = "weight_divider", nullable = false)
    private double weightDivider;

    @Getter
    @Setter
    @Column(name = "weight_modifier", nullable = false)
    private double weightModifier;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Getter
    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "slayer_key", nullable = false)
    private transient ConcurrentList<SkillLevelSqlModel> levels;

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
