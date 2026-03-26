package userapi.controllers;

import userapi.models.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final List<User> users = new ArrayList<>();

    @GetMapping
    public List<User> getUsers() {
        return users;
    }

    @PostMapping
    public User createUser(@RequestBody User userRequest) {
        User user = new User(UUID.randomUUID(), userRequest.getName(), userRequest.getEmail());
        users.add(user);
        return user;
    }
}