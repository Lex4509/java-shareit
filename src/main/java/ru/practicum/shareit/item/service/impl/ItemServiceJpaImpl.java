package ru.practicum.shareit.item.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotExistException;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingsAndComments;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ItemServiceJpaImpl implements ItemService {

    private final UserService userService;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public ItemServiceJpaImpl(UserService userService, ItemRepository itemRepository, BookingRepository bookingRepository, CommentRepository commentRepository) {
        this.userService = userService;
        this.itemRepository = itemRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional
    public ItemDto create(long userId, ItemDto itemDto) {
        userService.getById(userId);

        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userId);

        itemRepository.save(item);

        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDtoWithBookingsAndComments> findAll(long userId) {
        userService.getById(userId);
        List<Item> items = itemRepository.findByOwner(userId);
        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        return addBookingsAndCommentsForItems(items);
    }

    @Override
    @Transactional
    public @Valid ItemDto update(long itemId, ItemDto itemDto, long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotExistException("Item not exist"));

        if (userId != item.getOwner()) {
            throw new NotExistException("Item not belong to this user");
        }
        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDtoWithBookingsAndComments getById(long itemId, long userId) {
        userService.getById(userId);

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotExistException("Item not exist"));


        List<Item> list = List.of(item);
        ItemDtoWithBookingsAndComments itemDtoWithBookingsAndComments = addBookingsAndCommentsForItems(list).get(0);

        if (userId != item.getOwner()) {
            itemDtoWithBookingsAndComments.setLastBooking(null);
            itemDtoWithBookingsAndComments.setNextBooking(null);
        }

        return itemDtoWithBookingsAndComments;
    }

    @Override
    public List<ItemDto> search(String text, long userId) {
        userService.getById(userId);

        return ItemMapper.toListOfItemDto(itemRepository.findAvailableItemsByNameOrDescription(text.toLowerCase()));
    }

    @Override
    @Transactional
    public CommentDto addComment(long userId, long itemId, CommentCreateDto commentCreateDto) {
        UserDto userDto = userService.getById(userId);
        List<Booking> pastBookings = bookingRepository.findAllByBookerIdAndPastState(userId,
                Sort.by(Sort.Direction.DESC, "end"));
        if (pastBookings.isEmpty()) {
            throw new BadRequestException("Item has never been booked");
        }
        if (pastBookings.stream()
                .map(b -> b.getBooker().getId())
                .noneMatch(id -> id.equals(userId))) {
            throw new BadRequestException("Booker did not take this item or booking term has not expired");
        }
        Item item = pastBookings.stream()
                .map(Booking::getItem)
                .filter(i -> i.getId() == itemId)
                .findFirst()
                .orElseThrow();

        Comment comment = CommentMapper.toComment(commentCreateDto);
        comment.setItem(item);
        comment.setAuthor(UserMapper.toUser(userDto));
        comment.setCreated(Instant.now());

        commentRepository.save(comment);

        CommentDto commentDto = CommentMapper.toCommentDto(comment);
        commentDto.setAuthorName(userDto.getName());

        return commentDto;
    }

    private List<ItemDtoWithBookingsAndComments> addBookingsAndCommentsForItems(List<Item> items) {
        List<Long> itemsId = items.stream()
                .map(Item::getId)
                .collect(Collectors.toList());

        List<BookingForItemDto> bookings = bookingRepository.findAllByItemsId(itemsId).stream()
                .map(BookingMapper::toBookingDtoForItem)
                .collect(Collectors.toList());

        List<Comment> comments = commentRepository.findAllByItemsId(itemsId);

        Set<ItemDtoWithBookingsAndComments> itemsWithBookings =
                new TreeSet<>(Comparator.comparing(ItemDtoWithBookingsAndComments::getId));
        for (Item item : items) {
            ItemDtoWithBookingsAndComments itemDtoWithBookings = ItemMapper.toItemDtoWithBooking(item);
            Set<BookingForItemDto> nextBookings = new TreeSet<>(Comparator.comparing(BookingForItemDto::getStart));
            Set<BookingForItemDto> lastBookings = new TreeSet<>(Comparator.comparing(BookingForItemDto::getEnd)
                    .reversed());
            for (BookingForItemDto booking : bookings) {
                LocalDateTime now = LocalDateTime.now();
                if (item.getId() == booking.getItem().getId()) {
                    if (booking.getStart().isAfter(now)) {
                        nextBookings.add(booking);
                    } else {
                        lastBookings.add(booking);
                    }
                }
            }
            itemDtoWithBookings.setNextBooking(nextBookings.stream().findFirst().orElse(null));
            itemDtoWithBookings.setLastBooking(lastBookings.stream().findFirst().orElse(null));
            itemsWithBookings.add(itemDtoWithBookings);

            if (comments.isEmpty()) {
                itemDtoWithBookings.setComments(Collections.emptyList());
                continue;
            }

            itemDtoWithBookings.setComments(new ArrayList<>());
            for (Comment comment : comments) {
                if (comment.getItem().getId() == item.getId()) {
                    CommentDto commentDto = CommentMapper.toCommentDto(comment);
                    commentDto.setAuthorName(comment.getAuthor().getName());
                    itemDtoWithBookings.getComments().add(commentDto);
                }
            }
        }

        return new ArrayList<>(itemsWithBookings);
    }

}
