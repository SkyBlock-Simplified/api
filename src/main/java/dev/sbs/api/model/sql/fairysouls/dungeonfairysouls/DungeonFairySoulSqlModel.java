package dev.sbs.api.model.sql.fairysouls.dungeonfairysouls;

import dev.sbs.api.data.sql.model.SqlModel;
import dev.sbs.api.model.DungeonFairySoulModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "dungeon_fairy_souls")
public class DungeonFairySoulSqlModel implements DungeonFairySoulModel, SqlModel {


    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

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

}
