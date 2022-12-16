package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

/**
 * TODO Sprint add-controllers.
 */
public class ItemDto {

    private long id;
    private String name;
    private String description;
    private boolean isAvailable;
    private User owner;
    private ItemRequest request;

    public ItemDto(long id, String name, String description, boolean isAvailable, User owner, ItemRequest request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isAvailable = isAvailable;
        this.owner = owner;
        this.request = request;
    }
}
