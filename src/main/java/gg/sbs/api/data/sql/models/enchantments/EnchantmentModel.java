package gg.sbs.api.data.sql.models.enchantments;

import gg.sbs.api.data.sql.converters.ObjectMapConverter;
import gg.sbs.api.data.sql.SqlModel;
import gg.sbs.api.data.sql.models.SqlEffectsModel;
import gg.sbs.api.data.sql.models.itemtypes.ItemTypeModel;
import gg.sbs.api.util.builder.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "enchantments")
public class EnchantmentModel extends SqlEffectsModel implements SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "item_type", nullable = false)
    private ItemTypeModel itemType;

    @Getter
    @Setter
    @Column(name = "name", nullable = false, length = 127)
    private String name;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnchantmentModel)) return false;

        EnchantmentModel that = (EnchantmentModel) o;

        if (id != that.id) return false;
        if (itemLevel != that.itemLevel) return false;
        if (!itemType.equals(that.itemType)) return false;
        if (!name.equals(that.name)) return false;
        if (!effects.equals(that.effects)) return false;
        return updatedAt.equals(that.updatedAt);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.id).append(this.itemType).append(this.name).append(this.itemLevel).append(this.effects).append(this.updatedAt).build();
    }

}