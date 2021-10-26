package dev.sbs.api.model.sql.fairysouls;

import dev.sbs.api.data.sql.model.SqlModel;
import dev.sbs.api.model.FairySoulModel;
import dev.sbs.api.model.sql.locations.LocationSqlModel;
import dev.sbs.api.model.sql.locations.locationareas.LocationAreaSqlModel;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "fairy_souls")
public class FairySoulSqlModel implements FairySoulModel, SqlModel {

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
    @JoinColumn(name = "location_key", nullable = false)
    private LocationSqlModel location;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "location_area_key", nullable = false)
    private LocationAreaSqlModel locationArea;

    @Getter
    @Setter
    @Column(name = "walkable", nullable = false)
    private boolean walkable;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FairySoulSqlModel)) return false;

        FairySoulSqlModel that = (FairySoulSqlModel) o;

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
