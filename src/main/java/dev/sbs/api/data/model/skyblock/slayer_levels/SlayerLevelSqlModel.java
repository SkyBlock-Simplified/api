package dev.sbs.api.data.model.skyblock.slayer_levels;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.slayers.SlayerSqlModel;
import dev.sbs.api.data.sql.converter.map.StringObjectMapConverter;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(
    name = "skyblock_slayer_levels",
    indexes = {
        @Index(
            columnList = "slayer_key, level",
            unique = true
        ),
        @Index(
            columnList = "slayer_key"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SlayerLevelSqlModel implements SlayerLevelModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "slayer_key", nullable = false)
    private SlayerSqlModel slayer;

    @Getter
    @Setter
    @Column(name = "level", nullable = false)
    private Integer level;

    @Getter
    @Setter
    @Column(name = "total_exp_required", nullable = false)
    private Double totalExpRequired;

    @Getter
    @Setter
    @Column(name = "effects", nullable = false)
    @Convert(converter = StringObjectMapConverter.class)
    private Map<String, Object> effects;

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
