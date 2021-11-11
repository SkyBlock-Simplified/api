package dev.sbs.api.data.model.skills;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.items.ItemSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.Instant;

@Entity
@Transactional
@Table(name = "skills")
public class SkillSqlModel implements SkillModel, SqlModel {

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
    @Column(name = "description", nullable = false, length = 127)
    private String description;

    @Getter
    @Setter
    @Column(name = "max_level", nullable = false)
    private int maxLevel;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private ItemSqlModel item;

    @Getter
    @Setter
    @Column(name = "cosmetic", nullable = false)
    private boolean cosmetic;

    @Getter
    @Setter
    @Column(name = "weight_exponent", nullable = false)
    private double weightExponent;

    @Getter
    @Setter
    @Column(name = "weight_divider", nullable = false)
    private double weightDivider;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // TODO: Load Exp Table

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
