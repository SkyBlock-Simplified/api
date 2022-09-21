package dev.sbs.api.data.model.skyblock.npcs;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.location_data.location_areas.LocationAreaSqlModel;
import dev.sbs.api.data.model.skyblock.location_data.locations.LocationSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(
    name = "skyblock_npcs",
    indexes = {
        @Index(
            columnList = "x, y, z, key",
            unique = true
        ),
        @Index(
            columnList = "location_key"
        ),
        @Index(
            columnList = "location_area_key"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class NpcSqlModel implements NpcModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Getter
    @Setter
    @Column(name = "x")
    private Double x;

    @Getter
    @Setter
    @Column(name = "y")
    private Double y;

    @Getter
    @Setter
    @Column(name = "z")
    private Double z;

    @Getter
    @Setter
    @Id
    @Column(name = "key", nullable = false, length = 256)
    private String key;

    @Getter
    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "location_key")
    private LocationSqlModel location;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "location_area_key")
    private LocationAreaSqlModel locationArea;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NpcSqlModel that = (NpcSqlModel) o;

        return new EqualsBuilder()
            .append(this.getId(), that.getId())
            .append(this.getX(), that.getX())
            .append(this.getY(), that.getY())
            .append(this.getZ(), that.getZ())
            .append(this.getKey(), that.getKey())
            .append(this.getName(), that.getName())
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
            .append(this.getKey())
            .append(this.getName())
            .append(this.getLocation())
            .append(this.getLocationArea())
            .append(this.getUpdatedAt())
            .build();
    }

}
