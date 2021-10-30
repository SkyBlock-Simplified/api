package dev.sbs.api.model.sql.accessories.accessoryfamilies;

import dev.sbs.api.data.sql.model.SqlModel;
import dev.sbs.api.model.AccessoryFamilyModel;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "accessory_families")
public class AccessoryFamilySqlModel implements AccessoryFamilyModel, SqlModel {

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
    @Column(name = "reforges_stackable")
    private boolean reforgesStackable;

    @Getter
    @Setter
    @Column(name = "items_stackable")
    private boolean itemsStackable;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
        /*if (this == o) return true;
        if (!(o instanceof AccessoryFamilySqlModel)) return false;

        AccessoryFamilySqlModel that = (AccessoryFamilySqlModel) o;

        if (id != that.id) return false;
        if (reforgesStackable != that.reforgesStackable) return false;
        if (itemsStackable != that.itemsStackable) return false;
        if (!name.equals(that.name)) return false;
        return updatedAt.equals(that.updatedAt);*/
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
