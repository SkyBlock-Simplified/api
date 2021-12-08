package dev.sbs.api.data.model.skyblock.formats;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.sql.converter.ColorConverter;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.awt.*;
import java.time.Instant;

@Entity
@Table(
    name = "skyblock_formats"
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FormatSqlModel implements FormatModel, SqlModel {

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
    @Column(name = "code", nullable = false, length = 1)
    private char code;

    @Getter
    @Setter
    @Column(name = "rgb", length = 1)
    @Convert(converter = ColorConverter.class)
    private Color rgb;

    @Getter
    @Setter
    @Column(name = "format", nullable = false)
    private boolean format;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FormatSqlModel)) return false;
        FormatSqlModel that = (FormatSqlModel) o;

        return new EqualsBuilder().append(this.getCode(), that.getCode())
            .append(this.isFormat(), that.isFormat())
            .append(this.getId(), that.getId())
            .append(this.getKey(), that.getKey())
            .append(this.getRgb(), that.getRgb())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.getId()).append(this.getKey()).append(this.getCode()).append(this.getRgb()).append(this.isFormat()).append(this.getUpdatedAt()).build();
    }

}
