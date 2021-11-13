package dev.sbs.api.data.model.reforges;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.reforge_types.ReforgeTypeSqlModel;
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
        name = "reforges"
)
public class ReforgeSqlModel implements ReforgeModel, SqlModel {

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

        return new EqualsBuilder().append(this.getId(), that.getId()).append(this.isStone(), that.isStone()).append(this.getKey(), that.getKey()).append(this.getName(), that.getName()).append(this.getType(), that.getType()).append(this.getUpdatedAt(), that.getUpdatedAt()).build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.getId()).append(this.getKey()).append(this.getName()).append(this.getType()).append(this.isStone()).append(this.getUpdatedAt()).build();
    }

}
