package dev.sbs.api.data.model.skyblock.bestiary_data.bestiary_type_levels;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.bestiary_data.bestiary_types.BestiaryTypeSqlModel;
import dev.sbs.api.data.sql.converter.map.StringDoubleMapConverter;
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
    name = "skyblock_bestiary_type_levels",
    indexes = {
        @Index(
            columnList = "bestiary_type_key, level",
            unique = true
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BestiaryTypeLevelSqlModel implements BestiaryTypeLevelModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "bestiary_type_key", nullable = false)
    private BestiaryTypeSqlModel type;

    @Getter
    @Setter
    @Column(name = "level", nullable = false)
    private Integer level;

    @Getter
    @Setter
    @Column(name = "total_kills_required", nullable = false)
    private Integer totalKillsRequired;

    @Getter
    @Setter
    @Column(name = "effects")
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

        BestiaryTypeLevelSqlModel that = (BestiaryTypeLevelSqlModel) o;

        return new EqualsBuilder()
            .append(this.getId(), that.getId())
            .append(this.getType(), that.getType())
            .append(this.getLevel(), that.getLevel())
            .append(this.getTotalKillsRequired(), that.getTotalKillsRequired())
            .append(this.getEffects(), that.getEffects())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getType())
            .append(this.getLevel())
            .append(this.getTotalKillsRequired())
            .append(this.getEffects())
            .append(this.getUpdatedAt())
            .build();
    }

}
