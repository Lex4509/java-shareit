package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingsAndComments;

import java.util.List;

public interface ItemService {

    ItemDto create(long id, ItemDto itemDto);

    ItemDto update(long id, ItemDto itemDto, long userId);

    List<ItemDtoWithBookingsAndComments> findAll(long id);

    List<ItemDto> search(String text, long userId);

    ItemDtoWithBookingsAndComments getById(long id, long userID);

    CommentDto addComment(long userId, long itemId, CommentCreateDto commentCreateDto);

}
