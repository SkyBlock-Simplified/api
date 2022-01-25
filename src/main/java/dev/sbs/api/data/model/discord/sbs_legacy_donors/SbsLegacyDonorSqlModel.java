package dev.sbs.api.data.model.discord.sbs_legacy_donors;

import dev.sbs.api.data.model.SqlModel;
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
    name = "discord_sbs_legacy_donors"
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SbsLegacyDonorSqlModel implements SbsLegacyDonorModel, SqlModel {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Getter
    @Setter
    @Id
    @Column(name = "discord_id", nullable = false)
    private Long discordId;

    @Getter
    @Setter
    @Column(name = "amount", nullable = false)
    private Double amount;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

}
