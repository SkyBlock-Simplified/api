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
@Table(name = "enchantments")
public class EnchantmentSqlModel implements EnchantmentModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @Column(name = "key", nullable = false, length = 127)
    private String key;

    @Getter
    @Setter
    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "item_type_key", nullable = false, referencedColumnName = "key")
    private ReforgeTypeSqlModel itemType;

    @Getter
    @Setter
    @Column(name = "item_level", nullable = false)
    private int itemLevel;

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
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
        /*if (this == o) return true;
        if (!(o instanceof EnchantmentSqlModel)) return false;

        EnchantmentSqlModel that = (EnchantmentSqlModel) o;

        if (id != that.id) return false;
        if (itemLevel != that.itemLevel) return false;
        if (!itemType.equals(that.itemType)) return false;
        if (!name.equals(that.name)) return false;
        if (!effects.equals(that.effects)) return false;
        return updatedAt.equals(that.updatedAt);*/
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
