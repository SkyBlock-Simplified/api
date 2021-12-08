package dev.sbs.api.data.model.skyblock.fairy_exchanges;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.sql.converter.map.StringDoubleMapConverter;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(
    name = "skyblock_fairy_exchanges"
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FairyExchangeSqlModel implements FairyExchangeModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Getter
    @Setter
    @Id
    @Column(name = "exchange", nullable = false)
    private Integer exchange;

    @Getter
    @Setter
    @Column(name = "effects", nullable = false)
    @Convert(converter = StringDoubleMapConverter.class)
    private Map<String, Double> effects;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FairyExchangeSqlModel)) return false;
        FairyExchangeSqlModel that = (FairyExchangeSqlModel) o;

        return new EqualsBuilder().append(this.getId(), that.getId()).append(this.getExchange(), that.getExchange()).append(this.getEffects(), that.getEffects()).append(this.getUpdatedAt(), that.getUpdatedAt()).build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.getId()).append(this.getExchange()).append(this.getEffects()).append(this.getUpdatedAt()).build();
    }

}
