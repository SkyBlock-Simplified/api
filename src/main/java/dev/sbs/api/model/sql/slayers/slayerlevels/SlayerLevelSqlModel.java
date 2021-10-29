package dev.sbs.api.model.sql.slayers.slayerlevels;

import dev.sbs.api.data.sql.model.SqlModel;
import dev.sbs.api.model.SlayerLevelModel;
import dev.sbs.api.model.SlayerModel;
import dev.sbs.api.model.sql.slayers.SlayerSqlModel;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import dev.sbs.api.util.helper.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "slayer_levels")
public class SlayerLevelSqlModel implements SlayerLevelModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "slayer_key", nullable = false)
    private SlayerSqlModel slayer;

    @Getter
    @Setter
    @Column(name = "level", nullable = false)
    private int level;

    @Getter
    @Setter
    @Column(name = "total_exp_required", nullable = false)
    private double totalExpRequired;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SlayerLevelSqlModel)) return false;

        SlayerLevelSqlModel that = (SlayerLevelSqlModel) o;

        if (id != that.id) return false;
        if (level != that.level) return false;
        if (totalExpRequired != that.totalExpRequired) return false;
        return updatedAt.equals(that.updatedAt);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
