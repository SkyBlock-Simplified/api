package gg.sbs.api.data.sql.model.pets;

import gg.sbs.api.data.sql.model.SqlModel;
import gg.sbs.api.data.sql.model.rarities.RarityModel;
import gg.sbs.api.data.sql.model.skills.SkillModel;
import gg.sbs.api.util.StringUtil;
import gg.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "pets")
public class PetModel implements SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @Column(name = "item_id", nullable = false, length = 127)
    private String itemId;

    @Getter
    @Setter
    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "lowest_rarity", nullable = false)
    private RarityModel lowestRarity;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "skill", nullable = false)
    private SkillModel skill;

    @Getter
    @Setter
    @Column(name = "type", nullable = false)
    private int type;

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
        if (!(o instanceof PetModel)) return false;

        PetModel petModel = (PetModel) o;

        if (id != petModel.id) return false;
        if (!StringUtil.equals(this.itemId, petModel.itemId)) return false;
        if (!StringUtil.equals(this.name, petModel.name)) return false;
        if (!lowestRarity.equals(petModel.lowestRarity)) return false;
        if (!skill.equals(petModel.skill)) return false;
        if (type != petModel.type) return false;
        if (!StringUtil.equals(this.skin, petModel.skin)) return false;
        return updatedAt.equals(petModel.updatedAt);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.id).append(this.itemId).append(this.name).append(this.lowestRarity).append(this.skill).append(this.type).append(this.skin).append(this.updatedAt).build();
    }

}