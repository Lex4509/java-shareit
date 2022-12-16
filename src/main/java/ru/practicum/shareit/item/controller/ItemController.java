package ru.practicum.shareit.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto create(@Valid @RequestBody ItemDto itemDto, @RequestHeader(value = "X-Sharer-User-Id") long id){
        return itemService.create(id, itemDto);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@PathVariable long id,
                          @RequestBody ItemDto itemDto,
                          @RequestHeader(value = "X-Sharer-User-Id") long userId){
        return itemService.update(id, itemDto, userId);
    }

    @GetMapping
    public List<ItemDto> findAll(@RequestHeader(value = "X-Sharer-User-Id") long userId){
        return itemService.findAll(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam("text") String text){
        return itemService.search(text);
    }

    @GetMapping("{id}")
    public ItemDto getById(@PathVariable long id){
        return itemService.getById(id);
    }

}
