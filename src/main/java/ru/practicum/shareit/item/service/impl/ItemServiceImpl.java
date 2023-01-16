package ru.practicum.shareit.item.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotExistException;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingsAndComments;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemDao itemDao;
    private final UserService userService;

    @Autowired
    public ItemServiceImpl(ItemDao itemDao, UserService userService) {
        this.itemDao = itemDao;
        this.userService = userService;
    }

    @Override
    public ItemDto create(long id, ItemDto itemDto) {

        return ItemMapper.toItemDto(itemDao.create(ItemMapper.toItem(itemDto)));

    }

    @Override
    public ItemDto update(long id, ItemDto itemDto, long userId) {
        userService.getById(userId);
        return ItemMapper.toItemDto(itemDao.update(id, ItemMapper.toItem(itemDto)));
    }

    @Override
    public List<ItemDtoWithBookingsAndComments> findAll(long id) {
        return itemDao.findAll(id)
                .stream()
                .map(ItemMapper::toItemDtoWithBooking)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text, long userId) {
        return itemDao.search(text)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDtoWithBookingsAndComments getById(long id, long userID) {
        List<Item> items = itemDao.getById(id);
        if (items.size() < 1)
            throw new NotExistException("Item does not exist");
        return ItemMapper.toItemDtoWithBooking(items.get(0));
    }

    @Override
    public CommentDto addComment(long userId, long itemId, CommentCreateDto commentCreateDto) {
        return null;
    }
}
