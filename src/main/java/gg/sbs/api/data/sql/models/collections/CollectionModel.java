package gg.sbs.api.data.sql.models.collections;

import gg.sbs.api.data.sql.SqlModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "collections")
public class CollectionModel implements SqlModel {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @Column(name = "collection_key", nullable = false, length = 127)
    private String collectionKey;

    @Getter
    @Setter
    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CollectionModel)) return false;

        CollectionModel that = (CollectionModel) o;

        if (id != that.id) return false;
        if (!collectionKey.equals(that.collectionKey)) return false;
        if (!name.equals(that.name)) return false;
        return updatedAt.equals(that.updatedAt);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + collectionKey.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + updatedAt.hashCode();
        return result;
    }
}
