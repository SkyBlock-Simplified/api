package dev.sbs.api.model.sql.pets.petexpscales;

import dev.sbs.api.data.sql.model.SqlModel;
import dev.sbs.api.model.PetExpScaleModel;
import dev.sbs.api.model.sql.pets.pettypes.PetTypeSqlModel;
import dev.sbs.api.model.sql.rarities.RaritySqlModel;
import dev.sbs.api.model.sql.skills.SkillSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import dev.sbs.api.util.helper.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "pet_exp_scales")
public class PetExpScaleSqlModel implements PetExpScaleModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @Column(name = "name", nullable = false, length = 127)
    private int value;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
        /*if (this == o) return true;
        if (!(o instanceof PetExpScaleSqlModel)) return false;

        PetExpScaleSqlModel petModel = (PetExpScaleSqlModel) o;

        if (id != petModel.id) return false;
        if (value != petModel.value) return false;
        return updatedAt.equals(petModel.updatedAt);*/
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
