package dev.sbs.api.model.sql.npcs;

import dev.sbs.api.data.sql.model.SqlModel;
import dev.sbs.api.model.NpcModel;
import dev.sbs.api.model.sql.locations.LocationSqlModel;
import dev.sbs.api.model.sql.locations.locationareas.LocationAreaSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "npcs")
public class NpcSqlModel implements NpcModel, SqlModel {

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
    @Column(name = "name", nullable = false)
    private String name;

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
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
