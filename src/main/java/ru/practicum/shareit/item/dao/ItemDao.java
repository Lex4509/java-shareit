package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ItemDao {

    private final Set<Item> items = new HashSet<>();
    int generator = 1;

    public Item create(Item item) {
        item.setId(generator);
        generator++;
        items.add(item);
        return item;
    }

    public Item update(long id, Item item) {
        items.stream()
                .filter(currentItem -> currentItem.getId() == id)
                .forEach(currentItem -> {
                    if (item.getName() != null)
                        currentItem.setName(item.getName());
                    if (item.getDescription() != null)
                        currentItem.setDescription(item.getDescription());
                    if (item.isAvailable() != null)
                        currentItem.setAvailable(item.isAvailable());
                        }
                );
        return items.stream().filter(currentItem -> currentItem.getId() == id).collect(Collectors.toList()).get(0);
    }

    public List<Item> findAll(long id) {
        return null;
    }

    public List<Item> search(String text) {
        List<Item> itemList = new ArrayList<>();

        if (!text.isBlank()) {
            for (Item item : items) {
                String name = item.getName().toLowerCase();
                String description = item.getDescription().toLowerCase();

                if (name.contains(text.toLowerCase()) || description.contains(text.toLowerCase()) && item.isAvailable()) {
                    itemList.add(item);
                }
            }
        }

        return itemList;

    }

    public List<Item> getById(long id) {
        return items.stream()
                .filter(item -> item.getId() == id)
                .collect(Collectors.toList());
    }

}
