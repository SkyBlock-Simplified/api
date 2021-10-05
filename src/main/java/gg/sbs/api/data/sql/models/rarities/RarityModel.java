package gg.sbs.api.data.sql.models.rarities;

import gg.sbs.api.data.sql.SqlModel;
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
    @Column(name = "has_hypixel_name")
    private boolean hasHypixelName;

    @Getter
    @Setter
    @Column(name = "hypixel_name", length = 127)
    private String hypixelName;

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
        if (!Objects.equals(hypixelName, that.hypixelName)) return false;
        return updatedAt.equals(that.updatedAt);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + name.hashCode();
        result = 31 * result + (hasHypixelName ? 1 : 0);
        result = 31 * result + (hypixelName != null ? hypixelName.hashCode() : 0);
        result = 31 * result + updatedAt.hashCode();
        return result;
    }
}
