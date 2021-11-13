package dev.sbs.api.data.model.npcs;

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
        name = "npcs",
        indexes = {
                @Index(
                        columnList = "x, y, z, key",
                        unique = true
                ),
                @Index(
                        columnList = "location_key"
                ),
                @Index(
                        columnList = "loccation_area_key"
                )
        }
)
public class NpcSqlModel implements NpcModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;

    @Getter
    @Setter
    @Column(name = "x")
    private double x;

    @Getter
    @Setter
    @Column(name = "y")
    private double y;

    @Getter
    @Setter
    @Column(name = "z")
    private double z;

    @Getter
    @Setter
    @Id
    @Column(name = "key", nullable = false, length = 127)
    private String key;

    @Getter
    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_key")
    private LocationSqlModel location;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_area_key")
    private LocationAreaSqlModel locationArea;

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
