package gg.sbs.api.data.sql.model.stats;

import gg.sbs.api.data.sql.converter.UnicodeConverter;
import gg.sbs.api.data.sql.model.SqlModel;
import gg.sbs.api.data.sql.model.formats.FormatModel;
import gg.sbs.api.util.helper.StringUtil;
import gg.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "stats")
public class StatModel implements SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @Column(name = "stat_key", nullable = false, length = 127)
    private String key;

    @Getter
    @Setter
    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @Getter
    @Setter
    @Column(name = "symbol_code", nullable = false, length = 4)
    @Convert(converter = UnicodeConverter.class)
    private char symbol;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "format", nullable = false)
    private FormatModel format;

    @Getter
    @Setter
    @Column(name = "ordinal")
    private int ordinal;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatModel)) return false;

        StatModel that = (StatModel) o;

        if (id != that.id) return false;
        if (!StringUtil.equals(key, that.key)) return false;
        if (!StringUtil.equals(name, that.name)) return false;
        if (symbol != that.symbol) return false;
        if (!format.equals(that.format)) return false;
        if (ordinal != that.ordinal) return false;
        return updatedAt.equals(that.updatedAt);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}