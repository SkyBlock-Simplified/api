package dev.sbs.api.data.model.skyblock.rarities;

import dev.sbs.api.data.model.SqlModel;
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
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(
    name = "skyblock_rarities"
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RaritySqlModel implements RarityModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Getter
    @Setter
    @Id
    @Column(name = "key", nullable = false, length = 256)
    private String key;

    @Getter
    @Setter
    @Column(name = "name", nullable = false, length = 256)
    private String name;

    @Getter
    @Setter
    @Column(name = "ordinal", nullable = false)
    private Integer ordinal;

    @Getter
    @Setter
    @Column(name = "key_valid", nullable = false)
    private boolean keyValid;

    @Getter
    @Setter
    @Column(name = "pet_exp_offset")
    private Integer petExpOffset;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RaritySqlModel that = (RaritySqlModel) o;

        return new EqualsBuilder()
            .append(this.isKeyValid(), that.isKeyValid())
            .append(this.getId(), that.getId())
            .append(this.getKey(), that.getKey())
            .append(this.getName(), that.getName())
            .append(this.getOrdinal(), that.getOrdinal())
            .append(this.getPetExpOffset(), that.getPetExpOffset())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getKey())
            .append(this.getName())
            .append(this.getOrdinal())
            .append(this.isKeyValid())
            .append(this.getPetExpOffset())
            .append(this.getUpdatedAt())
            .build();
    }

}
