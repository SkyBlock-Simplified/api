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
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

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
    @Column(name = "ordinal")
    private Integer ordinal;

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
