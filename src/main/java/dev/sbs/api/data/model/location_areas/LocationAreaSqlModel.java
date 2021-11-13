package dev.sbs.api.data.model.location_areas;

import dev.sbs.api.data.model.SqlModel;
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
        name = "location_areas",
        indexes = {
                @Index(
                        columnList = "location_key"
                )
        }
)
public class LocationAreaSqlModel implements LocationAreaModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;

    @Getter
    @Setter
    @Id
    @Column(name = "key", nullable = false, length = 127)
    private String key;

    @Getter
    @Setter
    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_key", nullable = false)
    private LocationSqlModel location;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocationAreaSqlModel)) return false;
        LocationAreaSqlModel that = (LocationAreaSqlModel) o;

        return new EqualsBuilder().append(this.getId(), that.getId()).append(this.getKey(), that.getKey()).append(this.getName(), that.getName()).append(this.getLocation(), that.getLocation()).append(this.getUpdatedAt(), that.getUpdatedAt()).build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.getId()).append(this.getKey()).append(this.getName()).append(this.getLocation()).append(this.getUpdatedAt()).build();
    }

}
