package gg.sbs.api.data.sql.model.fairysouls;

import gg.sbs.api.data.sql.model.SqlModel;
import gg.sbs.api.data.sql.model.locations.LocationModel;
import gg.sbs.api.util.builder.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "fairysouls")
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
    @Column(name = "x", nullable = false)
    private double y;

    @Getter
    @Setter
    @Column(name = "x", nullable = false)
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
        return new HashCodeBuilder().append(this.id).append(this.x).append(this.y).append(this.z).append(this.updatedAt).build();
    }

}