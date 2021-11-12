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
import javax.transaction.Transactional;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_key", nullable = false)
    private LocationSqlModel location;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
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

        return new EqualsBuilder()
                .append(this.getId(), that.getId())
                .append(this.getX(), that.getX())
                .append(this.getY(), that.getY())
                .append(this.getZ(), that.getZ())
                .append(this.isWalkable(), that.isWalkable())
                .append(this.getLocation(), that.getLocation())
                .append(this.getLocationArea(), that.getLocationArea())
                .append(this.getUpdatedAt(), that.getUpdatedAt())
                .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.getId())
                .append(this.getX())
                .append(this.getY())
                .append(this.getZ())
                .append(this.getLocation())
                .append(this.getLocationArea())
                .append(this.isWalkable())
                .append(this.getUpdatedAt())
                .build();
    }

}
