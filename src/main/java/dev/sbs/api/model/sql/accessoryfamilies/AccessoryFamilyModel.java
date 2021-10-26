package dev.sbs.api.model.sql.accessoryfamilies;

import dev.sbs.api.data.sql.model.SqlModel;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "accessory_families")
public class AccessoryFamilyModel implements SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @Getter
    @Setter
    @Column(name = "reforges_stack")
    private boolean reforgesStack;

    @Getter
    @Setter
    @Column(name = "items_stack")
    private boolean itemsStack;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccessoryFamilyModel)) return false;

        AccessoryFamilyModel that = (AccessoryFamilyModel) o;

        if (id != that.id) return false;
        if (reforgesStack != that.reforgesStack) return false;
        if (itemsStack != that.itemsStack) return false;
        if (!name.equals(that.name)) return false;
        return updatedAt.equals(that.updatedAt);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
