package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ItemDao {

    Set<Item> items = new HashSet<>();
    int generator = 1;

    public Item create(Item item){
        item.setId(generator);
        generator++;
        items.add(item);
        return item;
    }

    public Item update(long id, Item item){
        items.stream()
                .filter(currentItem -> currentItem.getId()==id)
                .forEach(currentItem -> {
                    if (item.getName()!=null)
                        currentItem.setName(item.getName());
                    if (item.getDescription()!=null)
                        currentItem.setDescription(item.getDescription());
                    if (item.isAvailable() != null)
                        currentItem.setAvailable(item.isAvailable());
                        }
                );
        return items.stream().filter(currentItem -> currentItem.getId()==id).collect(Collectors.toList()).get(0);
    }

    public List<Item> findAll(long id){
        return items.stream()
                .filter(item->item.getOwner().getId()==id)
                .collect(Collectors.toList());
    }

    public List<Item> search(String text){
        return items.stream()
                .filter(item-> item.getName().toLowerCase().contains(text.toLowerCase())||
                        item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(Item::isAvailable)
                .collect(Collectors.toList());
    }

    public List<Item> getById(long id){
        return items.stream()
                .filter(item->item.getId()==id)
                .collect(Collectors.toList());
    }

}
