package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(value = "select i from Item i where i.owner = ?1")
    List<Item> findByOwner(Long userId);

    @Query(" select i from Item i" +
            " where lower(i.name) like (concat('%', ?1, '%'))" +
            " or lower(i.description) like (concat('%', ?1, '%'))" +
            " and i.isAvailable = true")
    List<Item> findAvailableItemsByNameOrDescription(String text);

}
