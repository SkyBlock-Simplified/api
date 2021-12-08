package dev.sbs.api.data.model.skyblock.skills;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.discord.bot_emojis.BotEmojiSqlModel;
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
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Getter
    @Setter
    @Id
    @Column(name = "key", nullable = false, length = 127)
    private String key;

    @Getter
    @Setter
    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @Getter
    @Setter
    @Column(name = "description", nullable = false, length = 127)
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
    @JoinColumn(name = "emoji_key")
    private BotEmojiSqlModel emoji;

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
    @SuppressWarnings("all")
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
