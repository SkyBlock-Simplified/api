package dev.sbs.api.model.sql.rarities;

import dev.sbs.api.data.sql.model.SqlModel;
import dev.sbs.api.model.RarityModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import dev.sbs.api.util.helper.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "rarities")
public class RaritySqlModel implements RarityModel, SqlModel {

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
    @Column(name = "ordinal")
    private int ordinal;

    @Getter
    @Setter
    @Column(name = "key_valid")
    private boolean keyValid;

    @Getter
    @Setter
    @Column(name = "pet_exp_offset")
    private int petExpOffset;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
        /*if (this == o) return true;
        if (!(o instanceof RaritySqlModel)) return false;

        RaritySqlModel that = (RaritySqlModel) o;

        if (id != that.id) return false;
        if (keyValid != that.keyValid) return false;
        if (!StringUtil.equals(key, that.key)) return false;
        if (!StringUtil.equals(name, that.name)) return false;
        return updatedAt.equals(that.updatedAt);*/
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
