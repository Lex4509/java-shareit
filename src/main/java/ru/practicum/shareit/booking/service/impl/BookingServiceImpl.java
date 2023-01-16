package ru.practicum.shareit.booking.service.impl;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotExistException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    public BookingServiceImpl(ItemRepository itemRepository, UserRepository userRepository, BookingRepository bookingRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    @Transactional
    public @Valid BookingDto create(long userId, BookingCreateDto bookingCreateDto) {
        Item item = itemRepository.findById(bookingCreateDto.getItemId())
                .orElseThrow(() -> new NotExistException("Item does not exist"));
        if (!item.isAvailable()) {
            throw new BadRequestException("Item is unavailable");
        }
        if (item.getOwner().equals(userId)) {
            throw new NotExistException("User's own item");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotExistException("User not exist"));
        Booking booking = BookingMapper.toBooking(bookingCreateDto, item, user);
        booking.setStatus(Status.WAITING);
        bookingRepository.save(booking);

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    @Transactional
    public @Valid BookingDto approve(long bookingId, boolean approved, long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotExistException("Booking not exist"));

        if (!booking.getItem().getOwner().equals(userId)) {
            throw new NotExistException("Item not own by this user");
        }
        if (booking.getStatus().equals(Status.APPROVED) && approved) {
            throw new BadRequestException("Booking is already approved");
        }

        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public @Valid BookingDto getById(long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotExistException("Booking not exist"));

        if (booking.getBooker().getId() != (userId) && booking.getItem().getOwner() != userId) {
            throw new NotExistException("Booking can not be viewed by this user");
        }

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAllByBooker(State state, long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotExistException("User not exist");
        }

        List<BookingDto> result = Collections.emptyList();

        switch (state) {
            case ALL:
                result = BookingMapper.toListOfBookingDto(bookingRepository.findAllByBookerId(userId,
                        Sort.by(Sort.Direction.DESC, "end")));
                break;
            case CURRENT:
                result = BookingMapper.toListOfBookingDto(bookingRepository
                        .findAllByBookerIdAndCurrentState(userId, Sort.by(Sort.Direction.DESC, "end")));
                break;
            case PAST:
                result = BookingMapper.toListOfBookingDto(bookingRepository
                        .findAllByBookerIdAndPastState(userId, Sort.by(Sort.Direction.DESC, "end")));
                break;
            case FUTURE:
                result = BookingMapper.toListOfBookingDto(bookingRepository
                        .findAllByBookerIdAndFutureState(userId, Sort.by(Sort.Direction.DESC, "end")));
                break;
            case WAITING:
                result = BookingMapper.toListOfBookingDto(bookingRepository
                        .findAllByBookerIdAndWaitingOrRejectedState(userId, Status.WAITING,
                                Sort.by(Sort.Direction.DESC, "end")));
                break;
            case REJECTED:
                result = BookingMapper.toListOfBookingDto(bookingRepository
                        .findAllByBookerIdAndWaitingOrRejectedState(userId, Status.REJECTED,
                                Sort.by(Sort.Direction.DESC, "end")));
                break;
        }
        return result;
    }

    @Override
    public List<BookingDto> getAllByOwner(State state, long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotExistException("User not exist");
        }

        List<BookingDto> result = Collections.emptyList();

        switch (state) {
            case ALL:
                result = BookingMapper.toListOfBookingDto(bookingRepository.findAllByOwnerId(userId,
                        Sort.by(Sort.Direction.DESC, "end")));
                break;
            case CURRENT:
                result = BookingMapper.toListOfBookingDto(bookingRepository
                        .findAllByOwnerIdAndCurrentState(userId,
                                Sort.by(Sort.Direction.DESC, "end")));
                break;
            case PAST:
                result = BookingMapper.toListOfBookingDto(bookingRepository
                        .findAllByOwnerIdAndPastState(userId,
                                Sort.by(Sort.Direction.DESC, "end")));
                break;
            case FUTURE:
                result = BookingMapper.toListOfBookingDto(bookingRepository
                        .findAllByOwnerIdAndFutureState(userId,
                                Sort.by(Sort.Direction.DESC, "end")));
                break;
            case WAITING:
                result = BookingMapper.toListOfBookingDto(bookingRepository
                        .findAllByOwnerIdAndWaitingOrRejectedState(userId, Status.WAITING,
                                Sort.by(Sort.Direction.DESC, "end")));
                break;
            case REJECTED:
                result = BookingMapper.toListOfBookingDto(bookingRepository
                        .findAllByOwnerIdAndWaitingOrRejectedState(userId, Status.REJECTED,
                                Sort.by(Sort.Direction.DESC, "end")));
                break;
        }
        return result;
    }
}
