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
import javax.transaction.Transactional;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dungeon_key", nullable = false)
    private DungeonSqlModel dungeon;

    @Getter
    @Setter
    @Column(name = "floor", nullable = false)
    private int floor;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_size_key", nullable = false)
    private DungeonFloorSizeSqlModel floorSize;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_boss_key", nullable = false)
    private DungeonBossSqlModel floorBoss;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DungeonFloorSqlModel)) return false;
        DungeonFloorSqlModel that = (DungeonFloorSqlModel) o;

        return new EqualsBuilder()
                .append(this.getId(), that.getId())
                .append(this.getFloor(), that.getFloor())
                .append(this.getDungeon(), that.getDungeon())
                .append(this.getFloorSize(), that.getFloorSize())
                .append(this.getFloorBoss(), that.getFloorBoss())
                .append(this.getUpdatedAt(), that.getUpdatedAt())
                .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.getId())
                .append(this.getDungeon())
                .append(this.getFloor())
                .append(this.getFloorSize())
                .append(this.getFloorBoss())
                .append(this.getUpdatedAt())
                .build();
    }

}
