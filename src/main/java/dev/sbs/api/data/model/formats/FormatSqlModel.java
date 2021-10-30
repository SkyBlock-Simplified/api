package dev.sbs.api.data.model.formats;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.sql.converter.ColorConverter;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.awt.*;
import java.time.Instant;

@Entity
@Table(name = "formats")
public class FormatSqlModel implements FormatModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
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
    @SuppressWarnings("all")
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
