package dev.sbs.api.data.model.skyblock.craftingtable_data.craftingtable_recipe_slots;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.craftingtable_data.craftingtable_recipes.CraftingTableRecipeSqlModel;
import dev.sbs.api.data.model.skyblock.craftingtable_data.craftingtable_slots.CraftingTableSlotSqlModel;
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
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CraftingTableRecipeSlotSqlModel implements CraftingTableRecipeSlotModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "recipe_key", nullable = false)
    private CraftingTableRecipeSqlModel recipe;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "slot_key", nullable = false)
    private CraftingTableSlotSqlModel slot;

    @Getter
    @Setter
    @Column(name = "ordinal", nullable = false)
    private Integer ordinal;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CraftingTableRecipeSlotSqlModel that = (CraftingTableRecipeSlotSqlModel) o;

        return new EqualsBuilder()
            .append(this.getId(), that.getId())
            .append(this.getRecipe(), that.getRecipe())
            .append(this.getSlot(), that.getSlot())
            .append(this.getOrdinal(), that.getOrdinal())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getRecipe())
            .append(this.getSlot())
            .append(this.getOrdinal())
            .append(this.getUpdatedAt())
            .build();
    }

}
