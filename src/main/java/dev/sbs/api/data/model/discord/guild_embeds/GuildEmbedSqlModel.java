package dev.sbs.api.data.model.discord.guild_embeds;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.discord.embed_types.EmbedTypeSqlModel;
import dev.sbs.api.data.model.discord.guilds.GuildSqlModel;
import dev.sbs.api.data.sql.converter.ColorConverter;
import dev.sbs.api.util.builder.EqualsBuilder;
import dev.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.awt.*;
import java.time.Instant;

@Entity
@Table(
    name = "discord_guild_embeds",
    indexes = {
        @Index(
            columnList = "guild_id, key",
            unique = true
        )
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GuildEmbedSqlModel implements GuildEmbedModel, SqlModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "guild_id", nullable = false)
    private GuildSqlModel guild;

    @Getter
    @Setter
    @Column(name = "key", nullable = false)
    private String key;

    @Getter
    @Setter
    @Column(name = "title", nullable = false)
    private String title;

    @Getter
    @Setter
    @Column(name = "type_key", nullable = false)
    private EmbedTypeSqlModel type;

    @Getter
    @Setter
    @Column(name = "color", nullable = false)
    @Convert(converter = ColorConverter.class)
    private Color color;

    @Getter
    @Setter
    @Column(name = "url")
    private String url;

    @Getter
    @Setter
    @Column(name = "description")
    private String description;

    @Getter
    @Setter
    @Column(name = "author_name")
    private String authorName;

    @Getter
    @Setter
    @Column(name = "author_url")
    private String authorUrl;

    @Getter
    @Setter
    @Column(name = "author_icon_url")
    private String authorIconUrl;

    @Getter
    @Setter
    @Column(name = "image_url")
    private String imageUrl;

    @Getter
    @Setter
    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Getter
    @Setter
    @Column(name = "video_url")
    private String videoUrl;

    @Getter
    @Setter
    @Column(name = "timestamp")
    private Instant timestamp;

    @Getter
    @Setter
    @Column(name = "footer_text")
    private String footerText;

    @Getter
    @Setter
    @Column(name = "footer_icon_url")
    private String footerIconUrl;

    @Getter
    @Setter
    @Column(name = "notes")
    private String notes;

    @Getter
    @Setter
    @Column(name = "submitter_discord_id")
    private Long submitterDiscordId;

    @Getter
    @Setter
    @Column(name = "editor_discord_id")
    private Long editorDiscordId;

    @Getter
    @Column(name = "submitted_at", nullable = false)
    private Instant submittedAt;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GuildEmbedSqlModel)) return false;
        GuildEmbedSqlModel that = (GuildEmbedSqlModel) o;

        return new EqualsBuilder().append(this.getId(), that.getId())
            .append(this.getGuild(), that.getGuild())
            .append(this.getKey(), that.getKey())
            .append(this.getTitle(), that.getTitle())
            .append(this.getType(), that.getType())
            .append(this.getColor(), that.getColor())
            .append(this.getUrl(), that.getUrl())
            .append(this.getDescription(), that.getDescription())
            .append(this.getAuthorName(), that.getAuthorName())
            .append(this.getAuthorUrl(), that.getAuthorUrl())
            .append(this.getAuthorIconUrl(), that.getAuthorIconUrl())
            .append(this.getImageUrl(), that.getImageUrl())
            .append(this.getThumbnailUrl(), that.getThumbnailUrl())
            .append(this.getVideoUrl(), that.getVideoUrl())
            .append(this.getTimestamp(), that.getTimestamp())
            .append(this.getFooterText(), that.getFooterText())
            .append(this.getFooterIconUrl(), that.getFooterIconUrl())
            .append(this.getNotes(), that.getNotes())
            .append(this.getSubmitterDiscordId(), that.getSubmitterDiscordId())
            .append(this.getEditorDiscordId(), that.getEditorDiscordId())
            .append(this.getSubmittedAt(), that.getSubmittedAt())
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.getId())
            .append(this.getGuild())
            .append(this.getKey())
            .append(this.getTitle())
            .append(this.getType())
            .append(this.getColor())
            .append(this.getUrl())
            .append(this.getDescription())
            .append(this.getAuthorName())
            .append(this.getAuthorUrl())
            .append(this.getAuthorIconUrl())
            .append(this.getImageUrl())
            .append(this.getThumbnailUrl())
            .append(this.getVideoUrl())
            .append(this.getTimestamp())
            .append(this.getFooterText())
            .append(this.getFooterIconUrl())
            .append(this.getNotes())
            .append(this.getSubmitterDiscordId())
            .append(this.getEditorDiscordId())
            .append(this.getSubmittedAt())
            .append(this.getUpdatedAt())
            .build();
    }

}
