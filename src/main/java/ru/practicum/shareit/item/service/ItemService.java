package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto create(long id, ItemDto itemDto);

    ItemDto update(long id, ItemDto itemDto, long userId);

    List<ItemDto> findAll(long id);

    List<ItemDto> search(String text);

    ItemDto getById(long id);

}
