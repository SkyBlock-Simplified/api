package dev.sbs.api.model.sql.pets.petitemstats;

import dev.sbs.api.data.sql.model.SqlModel;
import dev.sbs.api.model.PetItemStatModel;
import dev.sbs.api.model.sql.items.ItemSqlModel;
import dev.sbs.api.model.sql.stats.StatSqlModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "pet_item_stats")
public class PetItemStatSqlModel implements PetItemStatModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemSqlModel item;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "stat_key", nullable = false)
    private StatSqlModel stat;

    @Getter
    @Setter
    @Column(name = "stat_value", nullable = false)
    private int statValue;

    @Getter
    @Setter
    @Column(name = "base_value", nullable = false)
    private boolean percentage;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

}
