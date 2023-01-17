package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {

    BookingDto create(long userId, BookingCreateDto bookingCreateDto);

    BookingDto approve(long bookingId, boolean approved, long userId);

    BookingDto getById(long bookingId, long userId);

    List<BookingDto> getAllByBooker(String state, long userId);

    List<BookingDto> getAllByOwner(String state, long userId);

}
