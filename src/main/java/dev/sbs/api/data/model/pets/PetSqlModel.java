package dev.sbs.api.data.model.pets;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.pet_types.PetTypeSqlModel;
import dev.sbs.api.data.model.rarities.RaritySqlModel;
import dev.sbs.api.data.model.skills.SkillSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "pets")
public class PetSqlModel implements PetModel, SqlModel {

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
    @ManyToOne
    @JoinColumn(name = "lowest_rarity_key", nullable = false, referencedColumnName = "key")
    private RaritySqlModel lowestRarity;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "skill_key", nullable = false, referencedColumnName = "key")
    private SkillSqlModel skill;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "pet_type_key", nullable = false, referencedColumnName = "key")
    private PetTypeSqlModel petType;

    @Getter
    @Setter
    @Column(name = "skin", nullable = false)
    private String skin;

    @Getter
    @Setter
    @Column(name = "max_level", nullable = false)
    private int maxLevel = 100;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

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