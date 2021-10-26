package dev.sbs.api.model.sql.pets;

import dev.sbs.api.data.sql.model.SqlModel;
import dev.sbs.api.model.PetModel;
import dev.sbs.api.model.sql.pets.pettypes.PetTypeSqlModel;
import dev.sbs.api.model.sql.rarities.RaritySqlModel;
import dev.sbs.api.model.sql.skills.SkillSqlModel;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import dev.sbs.api.util.helper.StringUtil;
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
    @JoinColumn(name = "lowest_rarity_key", nullable = false)
    private RaritySqlModel lowestRarity;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "skill_key", nullable = false)
    private SkillSqlModel skill;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "pet_type_key", nullable = false)
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PetSqlModel)) return false;

        PetSqlModel petModel = (PetSqlModel) o;

        if (id != petModel.id) return false;
        if (!StringUtil.equals(this.key, petModel.key)) return false;
        if (!StringUtil.equals(this.name, petModel.name)) return false;
        if (!lowestRarity.equals(petModel.lowestRarity)) return false;
        if (!skill.equals(petModel.skill)) return false;
        if (!petType.equals(petModel.petType)) return false;
        if (!StringUtil.equals(this.skin, petModel.skin)) return false;
        return updatedAt.equals(petModel.updatedAt);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
