package gg.sbs.api.data.sql.model.rarities;

import gg.sbs.api.data.sql.model.SqlModel;
import gg.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "rarities")
public class RarityModel implements SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

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
    @Column(name = "rarity_tag", length = 127)
    private String rarityTag;

    @Getter
    @Setter
    @Column(name = "has_hypixel_name")
    private boolean hasHypixelName;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RarityModel)) return false;

        RarityModel that = (RarityModel) o;

        if (id != that.id) return false;
        if (hasHypixelName != that.hasHypixelName) return false;
        if (!name.equals(that.name)) return false;
        if (!Objects.equals(rarityTag, that.rarityTag)) return false;
        return updatedAt.equals(that.updatedAt);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.id).append(this.name).append(this.hasHypixelName).append(this.rarityTag).append(this.updatedAt).build();
    }

}