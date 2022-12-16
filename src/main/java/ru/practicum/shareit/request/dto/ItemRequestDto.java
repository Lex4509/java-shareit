package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
public class ItemRequestDto {

    private long id;
    private String description;
    private User requestor;
    private LocalDateTime created;

    public ItemRequestDto(long id, String description, User requestor, LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.requestor = requestor;
        this.created = created;
    }
}
