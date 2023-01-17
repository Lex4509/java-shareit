package ru.practicum.shareit.user.service.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.exception.NotExistException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.Marker;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
@Transactional (readOnly = true)
public class UserServiceJpaImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceJpaImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
    }

    @Override
    @Transactional
    public void delete(long id) {
        userRepository.delete(UserMapper.toUser(getById(id)));
    }

    @Override
    public UserDto getById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotExistException("User does not exist"));
        return UserMapper.toUserDto(user);
    }

    @Transactional
    @Override
    public @Validated(Marker.OnCreate.class) UserDto update(long id, UserDto userDto) {
        User oldUser = userRepository.findById(id)
                .orElseThrow(() -> new NotExistException("User does not exist"));
        User newUser = UserMapper.toUser(userDto);
        if (newUser.getName() != null && !newUser.getName().isBlank())
            oldUser.setName(newUser.getName());
        if (newUser.getEmail() != null && !newUser.getEmail().isBlank())
            oldUser.setEmail(newUser.getEmail());
        userRepository.save(oldUser);
        return UserMapper.toUserDto(oldUser);
    }

}
