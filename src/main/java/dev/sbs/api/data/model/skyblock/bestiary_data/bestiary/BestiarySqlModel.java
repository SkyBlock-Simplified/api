package dev.sbs.api.data.model.skyblock.bestiary_data.bestiary;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.bestiary_data.bestiary_brackets.BestiaryBracketSqlModel;
import dev.sbs.api.data.model.skyblock.bestiary_data.bestiary_categories.BestiaryCategorySqlModel;
import dev.sbs.api.data.sql.converter.list.IntegerListConverter;
import dev.sbs.api.util.builder.hash.EqualsBuilder;
import dev.sbs.api.util.builder.hash.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Getter
@Entity
@Table(
    name = "skyblock_bestiary",
    indexes = {
        @Index(
            columnList = "category_key, ordinal",
            unique = true
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BestiarySqlModel implements BestiaryModel, SqlModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Setter
    @Column(name = "key", nullable = false, unique = true)
    private String key;

    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Setter
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "bracket", nullable = false, referencedColumnName = "bracket"),
        @JoinColumn(name = "max_tier", nullable = false, referencedColumnName = "tier")
    })
    private BestiaryBracketSqlModel bracket;

    @Setter
    @ManyToOne
    @JoinColumn(name = "category_key", nullable = false)
    private BestiaryCategorySqlModel category;

    @Setter
    @Column(name = "ordinal", nullable = false)
    private Integer ordinal;

    @Setter
    @Column(name = "internal_name", nullable = false)
    private String internalName;

    @Setter
    @Convert(converter = IntegerListConverter.class)
    @Column(name = "levels", nullable = false)
    private List<Integer> levels;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @CreationTimestamp
    @Column(name = "submitted_at", nullable = false)
    private Instant submittedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BestiarySqlModel that = (BestiarySqlModel) o;

        return new EqualsBuilder()
            .append(this.getId(), that.getId())
            .append(this.getKey(), that.getKey())
            .append(this.getName(), that.getName())
            .append(this.getBracket(), that.getBracket())
            .append(this.getCategory(), that.getCategory())
            .append(this.getOrdinal(), that.getOrdinal())
            .append(this.getInternalName(), that.getInternalName())
            .append(this.getLevels(), that.getLevels())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .append(this.getSubmittedAt(), that.getSubmittedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getKey())
            .append(this.getName())
            .append(this.getBracket())
            .append(this.getCategory())
            .append(this.getOrdinal())
            .append(this.getInternalName())
            .append(this.getLevels())
            .append(this.getUpdatedAt())
            .append(this.getSubmittedAt())
            .build();
    }

}
