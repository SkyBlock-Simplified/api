package dev.sbs.api.data.model.discord.guild_data.guild_embeds;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.discord.guild_data.guilds.GuildSqlModel;
import dev.sbs.api.data.sql.converter.ColorConverter;
import dev.sbs.api.util.builder.hash.EqualsBuilder;
import dev.sbs.api.util.builder.hash.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.awt.*;
import java.time.Instant;

@Getter
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "guild_id", nullable = false)
    private GuildSqlModel guild;

    @Setter
    @Column(name = "key", nullable = false)
    private String key;

    @Setter
    @Column(name = "title", nullable = false)
    private String title;

    @Setter
    @Column(name = "color", nullable = false)
    @Convert(converter = ColorConverter.class)
    private Color color;

    @Setter
    @Column(name = "url")
    private String url;

    @Setter
    @Column(name = "description")
    private String description;

    @Setter
    @Column(name = "author_name")
    private String authorName;

    @Setter
    @Column(name = "author_url")
    private String authorUrl;

    @Setter
    @Column(name = "author_icon_url")
    private String authorIconUrl;

    @Setter
    @Column(name = "image_url")
    private String imageUrl;

    @Setter
    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Setter
    @Column(name = "video_url")
    private String videoUrl;

    @Setter
    @Column(name = "timestamp")
    private Instant timestamp;

    @Setter
    @Column(name = "footer_text")
    private String footerText;

    @Setter
    @Column(name = "footer_icon_url")
    private String footerIconUrl;

    @Setter
    @Column(name = "notes")
    private String notes;

    @Setter
    @Column(name = "submitter_discord_id")
    private Long submitterDiscordId;

    @Setter
    @Column(name = "editor_discord_id")
    private Long editorDiscordId;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @CreationTimestamp
    @Column(name = "submitted_at", nullable = false)
    private Instant submittedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GuildEmbedSqlModel that = (GuildEmbedSqlModel) o;

        return new EqualsBuilder()
            .append(this.getId(), that.getId())
            .append(this.getGuild(), that.getGuild())
            .append(this.getKey(), that.getKey())
            .append(this.getTitle(), that.getTitle())
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
            .append(this.getUpdatedAt(), that.getUpdatedAt())
            .append(this.getSubmittedAt(), that.getSubmittedAt())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getId())
            .append(this.getGuild())
            .append(this.getKey())
            .append(this.getTitle())
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
            .append(this.getUpdatedAt())
            .append(this.getSubmittedAt())
            .build();
    }

}
