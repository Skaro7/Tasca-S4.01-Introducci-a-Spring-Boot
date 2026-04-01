package userapi.repositories;

import userapi.models.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    List<User> findAll();
    Optional<User> findById(UUID id);
    List<User> searchByName(String name);
    boolean existsByEmail(String email);
}