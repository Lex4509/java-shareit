package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingInfoDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long owner;
    private Long requestId;
    private BookingInfoDto lastBooking;
    private BookingInfoDto nextBooking;
    private List<CommentDto> comments;

    public ItemDto() {
    }

    public ItemDto(Long id, String name, String description, Boolean available, Long userId, Long requestId) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = userId;
        this.requestId = requestId;

    }
}
