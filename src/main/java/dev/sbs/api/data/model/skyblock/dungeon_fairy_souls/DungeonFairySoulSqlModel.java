package dev.sbs.api.data.model.skyblock.dungeon_fairy_souls;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(
    name = "skyblock_dungeon_fairy_souls"
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DungeonFairySoulSqlModel implements DungeonFairySoulModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Getter
    @Setter
    @Column(name = "room", nullable = false, length = 127)
    private String room;

    @Getter
    @Setter
    @Column(name = "description", nullable = false)
    private String description;

    @Getter
    @Setter
    @Column(name = "where", nullable = false)
    private String where;

    @Getter
    @Setter
    @Column(name = "walkable", nullable = false)
    private boolean walkable;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DungeonFairySoulSqlModel that = (DungeonFairySoulSqlModel) o;

        return new EqualsBuilder()
            .append(this.isWalkable(), that.isWalkable())
            .append(this.getId(), that.getId())
            .append(this.getRoom(), that.getRoom())
            .append(this.getDescription(), that.getDescription())
            .append(this.getWhere(), that.getWhere())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getRoom())
            .append(this.getDescription())
            .append(this.getWhere())
            .append(this.isWalkable())
            .append(this.getUpdatedAt())
            .build();
    }

}
