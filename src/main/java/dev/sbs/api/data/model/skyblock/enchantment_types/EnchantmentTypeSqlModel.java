package dev.sbs.api.data.model.skyblock.enchantment_types;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.enchantments.EnchantmentSqlModel;
import dev.sbs.api.data.model.skyblock.reforge_types.ReforgeTypeSqlModel;
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
    name = "skyblock_enchantment_types",
    indexes = {
        @Index(
            columnList = "enchantment_key, reforge_type_key",
            unique = true
        ),
        @Index(
            columnList = "reforge_type_key"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EnchantmentTypeSqlModel implements EnchantmentTypeModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "enchantment_key", nullable = false)
    private EnchantmentSqlModel enchantment;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "reforge_type_key", nullable = false)
    private ReforgeTypeSqlModel reforgeType;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

}
