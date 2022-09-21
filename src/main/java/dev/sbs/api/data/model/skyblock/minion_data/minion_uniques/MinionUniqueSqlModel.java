package dev.sbs.api.data.model.skyblock.minion_data.minion_uniques;

import dev.sbs.api.data.model.SqlModel;
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
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(
    name = "skyblock_minion_uniques"
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MinionUniqueSqlModel implements MinionUniqueModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Getter
    @Setter
    @Column(name = "placeable", nullable = false, unique = true)
    private Integer placeable;

    @Getter
    @Setter
    @Column(name = "unique_crafts", nullable = false)
    private Integer uniqueCrafts;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MinionUniqueSqlModel that = (MinionUniqueSqlModel) o;

        return new EqualsBuilder()
            .append(this.getId(), that.getId())
            .append(this.getPlaceable(), that.getPlaceable())
            .append(this.getUniqueCrafts(), that.getUniqueCrafts())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getPlaceable())
            .append(this.getUniqueCrafts())
            .append(this.getUpdatedAt())
            .build();
    }

}
