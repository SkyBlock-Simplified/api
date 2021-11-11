package dev.sbs.api.data.model.dungeon_levels;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.dungeons.DungeonSqlModel;
import dev.sbs.api.data.model.slayers.SlayerSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "dungeon_levels")
public class DungeonLevelSqlModel implements DungeonLevelModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @Id
    @Column(name = "level", nullable = false, unique = true)
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
    @SuppressWarnings("all")
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
