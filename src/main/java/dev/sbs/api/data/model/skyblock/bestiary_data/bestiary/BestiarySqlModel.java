package dev.sbs.api.data.model.skyblock.bestiary_data.bestiary;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.bestiary_data.bestiary_families.BestiaryFamilySqlModel;
import dev.sbs.api.data.model.skyblock.bestiary_data.bestiary_types.BestiaryTypeSqlModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(
    name = "skyblock_bestiary",
    indexes = {
        @Index(
            columnList = "bestiary_type_key"
        ),
        @Index(
            columnList = "bestiary_family_key"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BestiarySqlModel implements BestiaryModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Getter
    @Setter
    @Id
    @Column(name = "key", nullable = false)
    private String key;

    @Getter
    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "bestiary_type_key", nullable = false)
    private BestiaryTypeSqlModel type;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "bestiary_family_key")
    private BestiaryFamilySqlModel family;

    @Getter
    @Setter
    @Column(name = "ordinal", nullable = false)
    private Integer ordinal;

    @Getter
    @Setter
    @Column(name = "max_level", nullable = false)
    private Integer maxLevel;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BestiarySqlModel that = (BestiarySqlModel) o;

        return new EqualsBuilder()
            .append(this.getId(), that.getId())
            .append(this.getKey(), that.getKey())
            .append(this.getName(), that.getName())
            .append(this.getType(), that.getType())
            .append(this.getFamily(), that.getFamily())
            .append(this.getOrdinal(), that.getOrdinal())
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
            .append(this.getType())
            .append(this.getFamily())
            .append(this.getOrdinal())
            .append(this.getMaxLevel())
            .append(this.getUpdatedAt())
            .build();
    }

}
