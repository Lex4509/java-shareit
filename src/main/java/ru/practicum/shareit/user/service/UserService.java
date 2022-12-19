package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> findAll();

    UserDto create(UserDto userDto);

    void delete(long id);

    UserDto getById(long id);

    UserDto update(long id, UserDto userDto);

}
