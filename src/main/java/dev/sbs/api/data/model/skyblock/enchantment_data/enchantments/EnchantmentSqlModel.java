package dev.sbs.api.data.model.skyblock.enchantment_data.enchantments;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.enchantment_data.enchantment_families.EnchantmentFamilySqlModel;
import dev.sbs.api.data.sql.converter.list.StringListConverter;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(
    name = "skyblock_enchantments",
    indexes = {
        @Index(
            columnList = "family_key"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EnchantmentSqlModel implements EnchantmentModel, SqlModel {

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
    @JoinColumn(name = "family_key", nullable = false)
    private EnchantmentFamilySqlModel family;

    @Getter
    @Setter
    @Column(name = "description", nullable = false)
    private String description;

    @Getter
    @Setter
    @Column(name = "mob_types", nullable = false)
    @Convert(converter = StringListConverter.class)
    private List<String> mobTypes;

    @Getter
    @Setter
    @Column(name = "required_level", nullable = false)
    private Integer requiredLevel;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EnchantmentSqlModel that = (EnchantmentSqlModel) o;

        return new EqualsBuilder()
            .append(this.getId(), that.getId())
            .append(this.getKey(), that.getKey())
            .append(this.getName(), that.getName())
            .append(this.getFamily(), that.getFamily())
            .append(this.getDescription(), that.getDescription())
            .append(this.getMobTypes(), that.getMobTypes())
            .append(this.getRequiredLevel(), that.getRequiredLevel())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getKey())
            .append(this.getName())
            .append(this.getFamily())
            .append(this.getDescription())
            .append(this.getMobTypes())
            .append(this.getRequiredLevel())
            .append(this.getUpdatedAt())
            .build();
    }

}
