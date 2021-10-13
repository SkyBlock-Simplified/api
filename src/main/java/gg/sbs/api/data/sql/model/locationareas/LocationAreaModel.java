package gg.sbs.api.data.sql.model.locationareas;

import gg.sbs.api.data.sql.model.SqlModel;
import gg.sbs.api.data.sql.model.locations.LocationModel;
import gg.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "location_areas")
public class LocationAreaModel implements SqlModel {

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
        if (!(o instanceof LocationAreaModel)) return false;

        LocationAreaModel that = (LocationAreaModel) o;

        if (id != that.id) return false;
        if (!name.equals(that.name)) return false;
        if (!location.equals(that.location)) return false;
        return updatedAt.equals(that.updatedAt);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}