package dev.sbs.api.data.model.skyblock.slayer_levels;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.slayers.SlayerSqlModel;
import dev.sbs.api.data.sql.converter.map.StringDoubleMapConverter;
import dev.sbs.api.util.builder.hash.EqualsBuilder;
import dev.sbs.api.util.builder.hash.HashCodeBuilder;
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
    @Convert(converter = StringDoubleMapConverter.class)
    private Map<String, Double> effects;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SlayerLevelSqlModel that = (SlayerLevelSqlModel) o;

        return new EqualsBuilder()
            .append(this.getId(), that.getId())
            .append(this.getSlayer(), that.getSlayer())
            .append(this.getLevel(), that.getLevel())
            .append(this.getTotalExpRequired(), that.getTotalExpRequired())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getSlayer())
            .append(this.getLevel())
            .append(this.getTotalExpRequired())
            .append(this.getUpdatedAt())
            .build();
    }

}
