package dev.sbs.api.model.sql.formats;

import dev.sbs.api.data.sql.model.SqlModel;
import dev.sbs.api.model.FormatModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import dev.sbs.api.util.helper.StringUtil;
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
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
        /*if (this == o) return true;
        if (!(o instanceof FormatSqlModel)) return false;

        FormatSqlModel that = (FormatSqlModel) o;

        if (id != that.id) return false;
        if (!StringUtil.equals(key, that.key)) return false;
        if (code != that.code) return false;
        if (!rgb.equals(that.rgb)) return false;
        if (format != that.format) return false;
        return updatedAt.equals(that.updatedAt);*/
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
