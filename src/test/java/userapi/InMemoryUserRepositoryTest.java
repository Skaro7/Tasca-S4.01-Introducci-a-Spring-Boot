package userapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import userapi.models.User;
import userapi.repositories.InMemoryUserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryUserRepositoryTest {

    private InMemoryUserRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryUserRepository();
    }

    @Test
    void save_addsUserToList() {
        User user = new User(UUID.randomUUID(), "Ada Lovelace", "ada@example.com");
        repository.save(user);
        assertThat(repository.findAll()).hasSize(1);
    }

    @Test
    void findAll_returnsAllUsers() {
        repository.save(new User(UUID.randomUUID(), "Ada Lovelace", "ada@example.com"));
        repository.save(new User(UUID.randomUUID(), "John Doe", "john@example.com"));
        assertThat(repository.findAll()).hasSize(2);
    }

    @Test
    void findById_returnsCorrectUser() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "Ada Lovelace", "ada@example.com");
        repository.save(user);
        Optional<User> found = repository.findById(id);
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Ada Lovelace");
    }

    @Test
    void findById_returnsEmptyIfNotFound() {
        Optional<User> found = repository.findById(UUID.randomUUID());
        assertThat(found).isEmpty();
    }

    @Test
    void searchByName_returnsMatchingUsers() {
        repository.save(new User(UUID.randomUUID(), "Ada Lovelace", "ada@example.com"));
        repository.save(new User(UUID.randomUUID(), "John Doe", "john@example.com"));
        List<User> result = repository.searchByName("ada");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Ada Lovelace");
    }

    @Test
    void existsByEmail_returnsTrueIfExists() {
        repository.save(new User(UUID.randomUUID(), "Ada Lovelace", "ada@example.com"));
        assertThat(repository.existsByEmail("ada@example.com")).isTrue();
    }

    @Test
    void existsByEmail_returnsFalseIfNotExists() {
        assertThat(repository.existsByEmail("noexiste@example.com")).isFalse();
    }
}