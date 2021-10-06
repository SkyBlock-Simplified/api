package gg.sbs.api.data.sql.models.pets;

import gg.sbs.api.data.sql.SqlModel;
import gg.sbs.api.data.sql.models.rarities.RarityModel;
import gg.sbs.api.data.sql.models.skills.SkillModel;
import gg.sbs.api.util.StringUtil;
import gg.sbs.api.util.builder.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.Map;

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
    private String item_id;

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
    @Column(name = "skill", nullable = false)
    private SkillModel skill;

    @Getter
    @Setter
    @Column(name = "type", nullable = false)
    private int type;

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
        if (!StringUtil.equals(this.item_id, petModel.item_id)) return false;
        if (!StringUtil.equals(this.name, petModel.name)) return false;
        if (!lowestRarity.equals(petModel.lowestRarity)) return false;
        if (!skill.equals(petModel.skill)) return false;
        if (type != petModel.type) return false;
        return updatedAt.equals(petModel.updatedAt);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.id).append(this.name).append(this.lowestRarity).append(this.skill).append(this.type).append(this.updatedAt).build();
    }

}