package dev.sbs.api.data.model.skyblock.potion_data.potion_brews;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.npcs.NpcSqlModel;
import dev.sbs.api.data.model.skyblock.rarities.RaritySqlModel;
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
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(
    name = "skyblock_potion_brews",
    indexes = {
        @Index(
            columnList = "key, amplified",
            unique = true
        ),
        @Index(
            columnList = "rarity_key"
        ),
        @Index(
            columnList = "source_npc_key"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PotionBrewSqlModel implements PotionBrewModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Getter
    @Setter
    @Column(name = "key", nullable = false, length = 256)
    private String key;

    @Getter
    @Setter
    @Column(name = "name", nullable = false, length = 256)
    private String name;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "rarity_key")
    private RaritySqlModel rarity;

    @Getter
    @Setter
    @Column(name = "description", nullable = false)
    private String description;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "source_npc_key")
    private NpcSqlModel npc;

    @Getter
    @Setter
    @Column(name = "coin_cost", nullable = false)
    private Integer coinCost;

    @Getter
    @Setter
    @Column(name = "amplified", nullable = false)
    private Integer amplified;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PotionBrewSqlModel that = (PotionBrewSqlModel) o;

        return new EqualsBuilder()
            .append(this.getId(), that.getId())
            .append(this.getKey(), that.getKey())
            .append(this.getName(), that.getName())
            .append(this.getRarity(), that.getRarity())
            .append(this.getDescription(), that.getDescription())
            .append(this.getNpc(), that.getNpc())
            .append(this.getCoinCost(), that.getCoinCost())
            .append(this.getAmplified(), that.getAmplified())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getKey())
            .append(this.getName())
            .append(this.getRarity())
            .append(this.getDescription())
            .append(this.getNpc())
            .append(this.getCoinCost())
            .append(this.getAmplified())
            .append(this.getUpdatedAt())
            .build();
    }

}
