package dev.sbs.api.data.model.skyblock.potion_data.potion_brew_buffs;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.potion_data.potion_brews.PotionBrewSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(
    name = "skyblock_potion_brew_buffs",
    indexes = {
        @Index(
            columnList = "potion_brew_key, buff_key, amplified",
            unique = true
        ),
        @Index(
            columnList = "potion_brew_key, amplified"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PotionBrewBuffSqlModel implements PotionBrewBuffModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;


    @Getter
    @Setter
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "potion_brew_key", nullable = false, referencedColumnName = "key"),
        @JoinColumn(name = "amplified", nullable = false, referencedColumnName = "amplified")
    })
    private PotionBrewSqlModel potionBrew;


    @Getter
    @Setter
    @Column(name = "buff_key", nullable = false, length = 256)
    private String buffKey;

    @Getter
    @Setter
    @Column(name = "buff_value", nullable = false, length = 256)
    private Double buffValue;

    @Getter
    @Setter
    @Column(name = "percentage", nullable = false)
    private boolean percentage;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PotionBrewBuffSqlModel that = (PotionBrewBuffSqlModel) o;

        return new EqualsBuilder()
            .append(this.isPercentage(), that.isPercentage())
            .append(this.getId(), that.getId())
            .append(this.getPotionBrew(), that.getPotionBrew())
            .append(this.getBuffKey(), that.getBuffKey())
            .append(this.getBuffValue(), that.getBuffValue())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getPotionBrew())
            .append(this.getBuffKey())
            .append(this.getBuffValue())
            .append(this.isPercentage())
            .append(this.getUpdatedAt())
            .build();
    }

}
