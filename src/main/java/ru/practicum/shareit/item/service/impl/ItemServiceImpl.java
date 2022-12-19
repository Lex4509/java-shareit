package ru.practicum.shareit.item.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotExistException;
import ru.practicum.shareit.item.dto.ItemDto;
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

        itemDto.setOwner(UserMapper.toUser(userService.getById(id)));
        return ItemMapper.toItemDto(itemDao.create(ItemMapper.toItem(itemDto)));

    }

    @Override
    public ItemDto update(long id, ItemDto itemDto, long userId) {
        userService.getById(userId);
        if (getById(id).getOwner().getId() != userId)
            throw new NotExistException("This item does not exist in current user items");
        return ItemMapper.toItemDto(itemDao.update(id, ItemMapper.toItem(itemDto)));
    }

    @Override
    public List<ItemDto> findAll(long id) {
        return itemDao.findAll(id)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text) {
        return itemDao.search(text)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getById(long id) {
        List<Item> items = itemDao.getById(id);
        if (items.size() < 1)
            throw new NotExistException("Item does not exist");
        return ItemMapper.toItemDto(items.get(0));
    }
}
