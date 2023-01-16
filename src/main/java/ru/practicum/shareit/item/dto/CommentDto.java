package ru.practicum.shareit.item.dto;

import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.Instant;

@Builder
public class CommentDto {

    private long id;
    @NotBlank
    private String text;
    @NotBlank
    private String authorName;
    @Past
    private Instant instant;

    public CommentDto(long id, String text, String authorName, Instant instant) {
        this.id = id;
        this.text = text;
        this.authorName = authorName;
        this.instant = instant;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Instant getInstant() {
        return instant;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }
}
