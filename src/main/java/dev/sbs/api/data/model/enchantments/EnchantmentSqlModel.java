package dev.sbs.api.data.model.enchantments;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.reforge_types.ReforgeTypeSqlModel;
import dev.sbs.api.data.sql.converter.ObjectMapConverter;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(
        name = "enchantments",
        indexes = {
                @Index(
                        columnList = "reforge_type_key"
                )
        }
)
public class EnchantmentSqlModel implements EnchantmentModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reforge_type_key", nullable = false)
    private ReforgeTypeSqlModel itemType;

    @Getter
    @Setter
    @Column(name = "level_required", nullable = false)
    private int requiredLevel;

    @Getter
    @Setter
    @Column(name = "effects")
    @Convert(converter = ObjectMapConverter.class)
    private Map<String, Object> effects;

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
