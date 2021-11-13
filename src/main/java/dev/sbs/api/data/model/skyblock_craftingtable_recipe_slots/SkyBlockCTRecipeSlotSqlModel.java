package dev.sbs.api.data.model.skyblock_craftingtable_recipe_slots;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock_craftingtable_recipes.SkyBlockCTRecipeSqlModel;
import dev.sbs.api.data.model.skyblock_craftingtable_slots.SkyBlockCTSlotSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(
        name = "skyblock_craftingtable_recipe_slots",
        indexes = {
                @Index(
                        columnList = "recipe_key, slot_key",
                        unique = true
                ),
                @Index(
                        columnList = "recipe_key, ordinal",
                        unique = true
                )
        }
)
public class SkyBlockCTRecipeSlotSqlModel implements SkyBlockCTRecipeSlotModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_key", nullable = false)
    private SkyBlockCTRecipeSqlModel recipe;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_key", nullable = false)
    private SkyBlockCTSlotSqlModel slot;

    @Getter
    @Setter
    @Column(name = "ordinal", nullable = false)
    private int ordinal;

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
