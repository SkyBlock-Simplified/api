package dev.sbs.api.data.model.skyblock.craftingtable_data.craftingtable_slots;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.util.builder.hash.EqualsBuilder;
import dev.sbs.api.util.builder.hash.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Getter
@Entity
@Table(
    name = "skyblock_craftingtable_slots"
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CraftingTableSlotSqlModel implements CraftingTableSlotModel, SqlModel {

    @Id
    @Setter
    @Column(name = "key")
    private String key;

    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Setter
    @Column(name = "slot", nullable = false)
    private Integer slot;

    @Setter
    @Column(name = "quick_craft", nullable = false)
    private boolean isQuickCraft;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @UpdateTimestamp
    @Column(name = "submitted_at", nullable = false)
    private Instant submittedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CraftingTableSlotSqlModel that = (CraftingTableSlotSqlModel) o;

        return new EqualsBuilder()
            .append(this.isQuickCraft(), that.isQuickCraft())
            .append(this.getKey(), that.getKey())
            .append(this.getName(), that.getName())
            .append(this.getSlot(), that.getSlot())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .append(this.getSubmittedAt(), that.getSubmittedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getKey())
            .append(this.getName())
            .append(this.getSlot())
            .append(this.isQuickCraft())
            .append(this.getUpdatedAt())
            .append(this.getSubmittedAt())
            .build();
    }

}
