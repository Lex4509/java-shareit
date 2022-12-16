package ru.practicum.shareit.user.repo;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserDao {

    int generator = 1;

    Set<User> users = new HashSet<>();

    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    public User create(User user){
        user.setId(generator);
        generator++;
        users.add(user);
        return user;
    }

    public void delete(long id){
        users.removeIf(user-> user.getId()==id);
    }

    public List<User> getById(long id){
        return users.stream()
                .filter(user->user.getId()==id)
                .collect(Collectors.toList());
    }

    public User update(long id, User newUser){
        users.stream()
                .filter(user->user.getId()==id)
                .forEach(user -> {
                    if (newUser.getName()!=null)
                        user.setName(newUser.getName());
                    if (newUser.getEmail()!=null)
                        user.setEmail(newUser.getEmail());
                });
        return users.stream().filter(item->item.getId()==id).collect(Collectors.toList()).get(0);
    }

    public boolean isUniqueEmail(User user){
        for (User usr : users) {
            if (usr.getEmail().equals(user.getEmail()))
                return false;
        }
        return true;
    }

}
