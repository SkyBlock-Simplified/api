package gg.sbs.api.data.sql.models.rarities;

import gg.sbs.api.data.sql.SqlModel;
import gg.sbs.api.data.sql.models.accessoryfamilies.AccessoryFamilyModel;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "rarities")
public class RarityModel extends SqlModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public boolean equals(Object o) {
        if (!(o instanceof RarityModel)) return false;
        return id == ((RarityModel) o).id;
    }

    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        return result;
    }
}
