package dev.sbs.api.model.sql.fairysouls;

import dev.sbs.api.data.sql.model.SqlModel;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import dev.sbs.api.model.sql.locations.LocationModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "fairy_souls")
public class FairySoulModel implements SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @Column(name = "x", nullable = false)
    private double x;

    @Getter
    @Setter
    @Column(name = "y", nullable = false)
    private double y;

    @Getter
    @Setter
    @Column(name = "z", nullable = false)
    private double z;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "location", nullable = false)
    private LocationModel location;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FairySoulModel)) return false;

        FairySoulModel that = (FairySoulModel) o;

        if (id != that.id) return false;
        if (x != that.x) return false;
        if (y != that.y) return false;
        if (z != that.z) return false;
        return updatedAt.equals(that.updatedAt);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
