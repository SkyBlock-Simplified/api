package dev.sbs.api.data.model.skyblock.reforge_conditions;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.items.ItemSqlModel;
import dev.sbs.api.data.model.skyblock.reforges.ReforgeSqlModel;
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
    name = "skyblock_reforge_conditions",
    indexes = {
        @Index(
            columnList = "reforge_key, item_id",
            unique = true
        ),
        @Index(
            columnList = "item_id"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ReforgeConditionSqlModel implements ReforgeConditionModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "reforge_key", nullable = false)
    private ReforgeSqlModel reforge;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemSqlModel item;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReforgeConditionSqlModel that = (ReforgeConditionSqlModel) o;

        return new EqualsBuilder()
            .append(this.getId(), that.getId())
            .append(this.getReforge(), that.getReforge())
            .append(this.getItem(), that.getItem())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getReforge())
            .append(this.getItem())
            .append(this.getUpdatedAt())
            .build();
    }

}