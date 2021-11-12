package dev.sbs.api.data.model.potion_brew_buffs;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.potion_brews.PotionBrewSqlModel;
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
        name = "potion_brew_buffs",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "buff_key",
                        columnNames = { "potion_brew_key", "buff_key" }
                )
        }
)
public class PotionBrewBuffSqlModel implements PotionBrewBuffModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "potion_brew_key", nullable = false)
    private PotionBrewSqlModel potionBrew;

    @Getter
    @Setter
    @Column(name = "buff_key", nullable = false, length = 127)
    private String buffKey;

    @Getter
    @Setter
    @Column(name = "buff_value", nullable = false, length = 127)
    private double buffValue;

    @Getter
    @Setter
    @Column(name = "percentage", nullable = false)
    private boolean percentage;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;


}
