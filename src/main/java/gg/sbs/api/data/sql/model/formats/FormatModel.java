package gg.sbs.api.data.sql.model.formats;

import gg.sbs.api.data.sql.model.SqlModel;
import gg.sbs.api.util.StringUtil;
import gg.sbs.api.util.builder.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.awt.*;
import java.time.Instant;

@Entity
@Table(name = "formats")
public class FormatModel implements SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @Column(name = "format_key", nullable = false, length = 127)
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FormatModel)) return false;

        FormatModel that = (FormatModel) o;

        if (id != that.id) return false;
        if (!StringUtil.equals(key, that.key)) return false;
        if (code != that.code) return false;
        if (!rgb.equals(that.rgb)) return false;
        if (format != that.format) return false;
        return updatedAt.equals(that.updatedAt);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.id).append(this.key).append(this.code).append(this.rgb).append(this.updatedAt).build();
    }

}