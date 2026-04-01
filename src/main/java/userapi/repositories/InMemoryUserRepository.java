package userapi.repositories;

import org.springframework.stereotype.Repository;
import userapi.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private final List<User> users = new ArrayList<>();

    @Override
    public User save(User user) {
        users.add(user);
        return user;
    }

    @Override
    public List<User> findAll() {
        return users;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<User> searchByName(String name) {
        return users.stream()
                .filter(u -> u.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    @Override
    public boolean existsByEmail(String email) {
        return users.stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }

    public void clear() {
        users.clear();
    }
}