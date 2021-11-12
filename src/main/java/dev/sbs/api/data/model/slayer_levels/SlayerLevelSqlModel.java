package dev.sbs.api.data.model.slayer_levels;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.slayers.SlayerSqlModel;
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
        name = "slayer_levels",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "slayer_level",
                        columnNames = { "slayer_key", "level" }
                )
        }
)
public class SlayerLevelSqlModel implements SlayerLevelModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slayer_key", nullable = false)
    private SlayerSqlModel slayer;

    @Getter
    @Setter
    @Column(name = "level", nullable = false)
    private int level;

    @Getter
    @Setter
    @Column(name = "total_exp_required", nullable = false)
    private double totalExpRequired;

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
