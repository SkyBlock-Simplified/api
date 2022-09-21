package dev.sbs.api.data.model.skyblock.bestiary_data.bestiary;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.skyblock.bestiary_data.bestiary_families.BestiaryFamilySqlModel;
import dev.sbs.api.data.model.skyblock.bestiary_data.bestiary_types.BestiaryTypeSqlModel;
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
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(
    name = "skyblock_bestiary",
    indexes = {
        @Index(
            columnList = "bestiary_type_key"
        ),
        @Index(
            columnList = "bestiary_family_key"
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BestiarySqlModel implements BestiaryModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Getter
    @Setter
    @Id
    @Column(name = "key", nullable = false)
    private String key;

    @Getter
    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "bestiary_type_key", nullable = false)
    private BestiaryTypeSqlModel type;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "bestiary_family_key")
    private BestiaryFamilySqlModel family;

    @Getter
    @Setter
    @Column(name = "ordinal", nullable = false)
    private Integer ordinal;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;



}
