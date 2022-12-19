package ru.practicum.shareit.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotExistException;
import ru.practicum.shareit.exception.NotUniqueException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<UserDto> findAll() {
        return userDao.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto create(UserDto userDto) {
        if (!userDao.isUniqueEmail(UserMapper.toUser(userDto)))
            throw new NotUniqueException("Not unique email");
        return UserMapper.toUserDto(userDao.create(UserMapper.toUser(userDto)));
    }

    @Override
    public void delete(long id) {
        getById(id);
        userDao.delete(id);
    }

    @Override
    public UserDto update(long id, UserDto userDto) {
        getById(id);
        if (!userDao.isUniqueEmail(UserMapper.toUser(userDto)))
            throw new NotUniqueException("Not unique email");
        return UserMapper.toUserDto(userDao.update(id, UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto getById(long id) {
        List<User> users = userDao.getById(id);
        if ((users.size()) < 1)
            throw new NotExistException("User does not exist");
        return UserMapper.toUserDto(users.get(0));
    }


}
