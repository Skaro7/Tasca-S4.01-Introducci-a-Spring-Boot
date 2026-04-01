package userapi.services;

import userapi.models.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(User user);
    List<User> getAllUsers();
    List<User> getUsersByName(String name);
    User getUserById(UUID id);
}