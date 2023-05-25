package dev.sbs.api.data.model.skyblock.dungeon_data.dungeon_floors;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.dungeon_data.dungeon_bosses.DungeonBossSqlModel;
import dev.sbs.api.data.model.skyblock.dungeon_data.dungeon_floor_sizes.DungeonFloorSizeSqlModel;
import dev.sbs.api.data.model.skyblock.dungeon_data.dungeons.DungeonSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(
    name = "skyblock_dungeon_floors",
    indexes = {
        @Index(
            columnList = "dungeon_key, floor",
            unique = true
        ),
        @Index(
            columnList = "floor_size_key"
        ),
        @Index(
            columnList = "floor_boss_key"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DungeonFloorSqlModel implements DungeonFloorModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "dungeon_key", nullable = false)
    private DungeonSqlModel dungeon;

    @Getter
    @Setter
    @Column(name = "floor", nullable = false)
    private Integer floor;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "floor_size_key")
    private DungeonFloorSizeSqlModel floorSize;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "floor_boss_key")
    private DungeonBossSqlModel floorBoss;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DungeonFloorSqlModel that = (DungeonFloorSqlModel) o;

        return new EqualsBuilder()
            .append(this.getId(), that.getId())
            .append(this.getDungeon(), that.getDungeon())
            .append(this.getFloor(), that.getFloor())
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
