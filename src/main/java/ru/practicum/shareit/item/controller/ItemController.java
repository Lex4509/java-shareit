package ru.practicum.shareit.item.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingsAndComments;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(@Qualifier("itemServiceJpaImpl") ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto save(@RequestHeader("X-Sharer-User-Id") long userId,
                        @RequestBody @Valid ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable @Positive long itemId,
                          @RequestHeader("X-Sharer-User-Id") long userId,
                          @RequestBody ItemDto itemDto) {
        return itemService.update(itemId, itemDto, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoWithBookingsAndComments getItemById(@PathVariable @Positive long itemId,
                                                      @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getById(itemId, userId);
    }

    @GetMapping
    public List<ItemDtoWithBookingsAndComments> getAllUserItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.findAll(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader("X-Sharer-User-Id") long userId,
                                @RequestParam String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }

        return itemService.search(text, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @PathVariable @Positive long itemId,
                                 @RequestBody @Valid CommentCreateDto commentCreationDto) {
        System.out.println("123134124521459837498124yhidbnv liajn vliqejkrbnfgliqwuehfquilwrjgnliwuJENBILUWJNVGLKAJ");
        return itemService.addComment(userId, itemId, commentCreationDto);
    }
}
