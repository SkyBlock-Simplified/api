package dev.sbs.api.data.model.skyblock.pet_data.pets;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.discord.emojis.EmojiSqlModel;
import dev.sbs.api.data.model.skyblock.pet_data.pet_types.PetTypeSqlModel;
import dev.sbs.api.data.model.skyblock.rarities.RaritySqlModel;
import dev.sbs.api.data.model.skyblock.skills.SkillSqlModel;
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
    name = "skyblock_pets",
    indexes = {
        @Index(
            columnList = "lowest_rarity_key"
        ),
        @Index(
            columnList = "skill_key"
        ),
        @Index(
            columnList = "pet_type_key"
        ),
        @Index(
            columnList = "emoji_key"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PetSqlModel implements PetModel, SqlModel {

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
    @ManyToOne
    @JoinColumn(name = "lowest_rarity_key")
    private RaritySqlModel lowestRarity;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "skill_key")
    private SkillSqlModel skill;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "pet_type_key")
    private PetTypeSqlModel petType;

    @Getter
    @Setter
    @Column(name = "skin", nullable = false)
    private String skin;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "emoji_key")
    private EmojiSqlModel emoji;

    @Getter
    @Setter
    @Column(name = "max_level", nullable = false)
    private Integer maxLevel = 100;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PetSqlModel that = (PetSqlModel) o;

        return new EqualsBuilder()
            .append(this.getId(), that.getId())
            .append(this.getKey(), that.getKey())
            .append(this.getName(), that.getName())
            .append(this.getLowestRarity(), that.getLowestRarity())
            .append(this.getSkill(), that.getSkill())
            .append(this.getPetType(), that.getPetType())
            .append(this.getSkin(), that.getSkin())
            .append(this.getEmoji(), that.getEmoji())
            .append(this.getMaxLevel(), that.getMaxLevel())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getKey())
            .append(this.getName())
            .append(this.getLowestRarity())
            .append(this.getSkill())
            .append(this.getPetType())
            .append(this.getSkin())
            .append(this.getEmoji())
            .append(this.getMaxLevel())
            .append(this.getUpdatedAt())
            .build();
    }

}
