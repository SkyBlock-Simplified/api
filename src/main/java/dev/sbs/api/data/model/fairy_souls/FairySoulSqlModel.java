package dev.sbs.api.data.model.fairy_souls;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.location_areas.LocationAreaSqlModel;
import dev.sbs.api.data.model.locations.LocationSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(
        name = "fairy_souls",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "fairy_soul_coordinates",
                        columnNames = { "x", "y", "z", "location_key" }
                )
        }
)
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
    @JoinColumn(name = "location_key", nullable = false, referencedColumnName = "key")
    private LocationSqlModel location;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "location_area_key", nullable = false, referencedColumnName = "key")
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
    @SuppressWarnings("all")
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
