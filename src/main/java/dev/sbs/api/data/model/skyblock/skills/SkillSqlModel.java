package dev.sbs.api.data.model.skyblock.skills;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.discord.emojis.EmojiSqlModel;
import dev.sbs.api.data.model.skyblock.items.ItemSqlModel;
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
    name = "skyblock_skills",
    indexes = {
        @Index(
            columnList = "item_id"
        ),
        @Index(
            columnList = "emoji_key"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SkillSqlModel implements SkillModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Getter
    @Setter
    @Id
    @Column(name = "key", nullable = false, length = 256)
    private String key;

    @Getter
    @Setter
    @Column(name = "name", nullable = false, length = 256)
    private String name;

    @Getter
    @Setter
    @Column(name = "description", nullable = false, length = 256)
    private String description;

    @Getter
    @Setter
    @Column(name = "max_level", nullable = false)
    private Integer maxLevel;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemSqlModel item;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "emoji_key", referencedColumnName = "key")
    private EmojiSqlModel emoji;

    @Getter
    @Setter
    @Column(name = "cosmetic", nullable = false)
    private boolean cosmetic;

    @Getter
    @Setter
    @Column(name = "weight_exponent", nullable = false)
    private Double weightExponent;

    @Getter
    @Setter
    @Column(name = "weight_divider", nullable = false)
    private Double weightDivider;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SkillSqlModel that = (SkillSqlModel) o;

        return new EqualsBuilder()
            .append(this.isCosmetic(), that.isCosmetic())
            .append(this.getId(), that.getId())
            .append(this.getKey(), that.getKey())
            .append(this.getName(), that.getName())
            .append(this.getDescription(), that.getDescription())
            .append(this.getMaxLevel(), that.getMaxLevel())
            .append(this.getItem(), that.getItem())
            .append(this.getEmoji(), that.getEmoji())
            .append(this.getWeightExponent(), that.getWeightExponent())
            .append(this.getWeightDivider(), that.getWeightDivider())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getKey())
            .append(this.getName())
            .append(this.getDescription())
            .append(this.getMaxLevel())
            .append(this.getItem())
            .append(this.getEmoji())
            .append(this.isCosmetic())
            .append(this.getWeightExponent())
            .append(this.getWeightDivider())
            .append(this.getUpdatedAt())
            .build();
    }

}
