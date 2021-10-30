package dev.sbs.api.data.model.dungeon_fairy_souls;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
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

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
