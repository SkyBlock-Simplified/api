package dev.sbs.api.data.model.skyblock.stats;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.formats.FormatSqlModel;
import dev.sbs.api.data.sql.converter.UnicodeConverter;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(
    name = "skyblock_stats",
    indexes = {
        @Index(
            columnList = "format_key"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StatSqlModel implements StatModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Getter
    @Setter
    @Id
    @Column(name = "key", nullable = false, length = 256)
    private String key;

    @Getter
    @Setter
    @Column(name = "name", nullable = false, length = 256)
    private String name;

    @Getter
    @Setter
    @Column(name = "symbol_code", length = 4)
    @Convert(converter = UnicodeConverter.class)
    private char symbol;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "format_key", nullable = false)
    private FormatSqlModel format;

    @Getter
    @Setter
    @Column(name = "multipliable")
    private boolean multipliable;

    @Getter
    @Setter
    @Column(name = "tunable")
    private boolean tunable;

    @Getter
    @Setter
    @Column(name = "ordinal")
    private Integer ordinal;

    @Getter
    @Setter
    @Column(name = "base_value")
    private Integer baseValue;

    @Getter
    @Setter
    @Column(name = "max_value")
    private Integer maxValue;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatSqlModel that = (StatSqlModel) o;

        return new EqualsBuilder()
            .append(this.getSymbol(), that.getSymbol())
            .append(this.isMultipliable(), that.isMultipliable())
            .append(this.getId(), that.getId())
            .append(this.getKey(), that.getKey())
            .append(this.getName(), that.getName())
            .append(this.getFormat(), that.getFormat())
            .append(this.getOrdinal(), that.getOrdinal())
            .append(this.getBaseValue(), that.getBaseValue())
            .append(this.getMaxValue(), that.getMaxValue())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getKey())
            .append(this.getName())
            .append(this.getSymbol())
            .append(this.getFormat())
            .append(this.isMultipliable())
            .append(this.getOrdinal())
            .append(this.getBaseValue())
            .append(this.getMaxValue())
            .append(this.getUpdatedAt())
            .build();
    }

}
