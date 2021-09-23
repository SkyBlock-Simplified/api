package gg.sbs.api.database.models.accessoryfamilies;

import gg.sbs.api.database.models.SqlModel;
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

    public void setId(long id) {
        this.id = id;
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

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
