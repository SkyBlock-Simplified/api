package gg.sbs.api.data.sql.models.accessoryfamilies;

import gg.sbs.api.data.sql.SqlModel;
import gg.sbs.api.data.sql.models.accessories.AccessoryModel;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "accessory_families")
public class AccessoryFamilyModel implements SqlModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @Column(name = "reforges_stack")
    private boolean reforgesStack;

    @Column(name = "items_stack")
    private boolean itemsStack;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isReforgesStack() {
        return reforgesStack;
    }

    public void setReforgesStack(boolean reforgesStack) {
        this.reforgesStack = reforgesStack;
    }

    public boolean isItemsStack() {
        return itemsStack;
    }

    public void setItemsStack(boolean itemsStack) {
        this.itemsStack = itemsStack;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public boolean equals(Object o) {
        if (!(o instanceof AccessoryFamilyModel)) return false;
        return id == ((AccessoryFamilyModel) o).id;
    }

    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (reforgesStack ? 1 : 0);
        result = 31 * result + (itemsStack ? 1 : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        return result;
    }
}
