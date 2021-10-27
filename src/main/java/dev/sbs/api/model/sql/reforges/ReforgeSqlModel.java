package dev.sbs.api.model.sql.reforges;

import dev.sbs.api.data.sql.model.SqlModel;
import dev.sbs.api.model.ReforgeModel;
import dev.sbs.api.model.sql.reforges.reforgetypes.ReforgeTypeSqlModel;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "reforges")
public class ReforgeSqlModel implements ReforgeModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @Column(name = "key", nullable = false, length = 127)
    private String key;

    @Getter
    @Setter
    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "reforge_type_key", nullable = false)
    private ReforgeTypeSqlModel type;

    @Getter
    @Setter
    @Column(name = "stone", nullable = false)
    private boolean stone;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReforgeSqlModel)) return false;

        ReforgeSqlModel that = (ReforgeSqlModel) o;

        if (id != that.id) return false;
        if (stone != that.stone) return false;
        if (!name.equals(that.name)) return false;
        return updatedAt.equals(that.updatedAt);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
