package dev.sbs.api.data.model.skyblock.enchantments;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.enchantment_families.EnchantmentFamilySqlModel;
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
    @ManyToOne
    @JoinColumn(name = "family_key", nullable = false)
    private EnchantmentFamilySqlModel family;

    @Getter
    @Setter
    @Column(name = "description", nullable = false)
    private String description;

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
        if (!(o instanceof EnchantmentSqlModel)) return false;
        EnchantmentSqlModel that = (EnchantmentSqlModel) o;

        return new EqualsBuilder().append(this.getId(), that.getId())
            .append(this.getKey(), that.getKey())
            .append(this.getName(), that.getName())
            .append(this.getFamily(), that.getFamily())
            .append(this.getDescription(), that.getDescription())
            .append(this.getRequiredLevel(), that.getRequiredLevel())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.getId()).append(this.getKey()).append(this.getName()).append(this.getFamily()).append(this.getDescription()).append(this.getRequiredLevel()).append(this.getUpdatedAt()).build();
    }

}
