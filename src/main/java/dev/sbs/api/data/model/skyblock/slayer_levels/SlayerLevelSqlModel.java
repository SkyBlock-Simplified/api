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

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;
import java.util.Map;

@Getter
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

    @Id
    @Setter
    @ManyToOne
    @JoinColumn(name = "slayer_key", referencedColumnName = "key")
    private SlayerSqlModel slayer;

    @Id
    @Setter
    @Column(name = "level")
    private Integer level;

    @Setter
    @Column(name = "total_exp_required", nullable = false)
    private Double totalExpRequired;

    @Setter
    @Column(name = "effects", nullable = false)
    @Convert(converter = StringDoubleMapConverter.class)
    private Map<String, Double> effects;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @UpdateTimestamp
    @Column(name = "submitted_at", nullable = false)
    private Instant submittedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SlayerLevelSqlModel that = (SlayerLevelSqlModel) o;

        return new EqualsBuilder()
            .append(this.getSlayer(), that.getSlayer())
            .append(this.getLevel(), that.getLevel())
            .append(this.getTotalExpRequired(), that.getTotalExpRequired())
            .append(this.getEffects(), that.getEffects())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .append(this.getSubmittedAt(), that.getSubmittedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getSlayer())
            .append(this.getLevel())
            .append(this.getTotalExpRequired())
            .append(this.getEffects())
            .append(this.getUpdatedAt())
            .append(this.getSubmittedAt())
            .build();
    }

}
