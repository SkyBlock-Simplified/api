package dev.sbs.api.data.model.dungeon_floors;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.dungeon_bosses.DungeonBossSqlModel;
import dev.sbs.api.data.model.dungeon_floor_sizes.DungeonFloorSizeSqlModel;
import dev.sbs.api.data.model.dungeons.DungeonSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(
        name = "dungeon_floors",
        uniqueConstraints = {
                @UniqueConstraint(name = "dungeon_floor", columnNames = { "dungeon_key", "floor" })
        }
)
public class DungeonFloorSqlModel implements DungeonFloorModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "dungeon_key", nullable = false, referencedColumnName = "key")
    private DungeonSqlModel dungeon;

    @Getter
    @Setter
    @Column(name = "floor", nullable = false)
    private int floor;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "floor_size_key", nullable = false, referencedColumnName = "key")
    private DungeonFloorSizeSqlModel floorSize;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "floor_boss_key", nullable = false, referencedColumnName = "key")
    private DungeonBossSqlModel floorBoss;

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
