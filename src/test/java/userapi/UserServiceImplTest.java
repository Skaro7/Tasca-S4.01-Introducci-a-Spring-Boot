package userapi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import userapi.models.User;
import userapi.repositories.UserRepository;
import userapi.services.UserServiceImpl;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_shouldThrowExceptionWhenEmailAlreadyExists() {
        // GIVEN
        User user = new User(null, "Ada Lovelace", "ada@example.com");
        when(userRepository.existsByEmail("ada@example.com")).thenReturn(true);

        // WHEN & THEN
        assertThatThrownBy(() -> userService.createUser(user))
                .isInstanceOf(EmailAlreadyExistsException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_shouldSaveUserWhenEmailIsNew() {
        // GIVEN
        User user = new User(null, "Ada Lovelace", "ada@example.com");
        when(userRepository.existsByEmail("ada@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        User result = userService.createUser(user);

        // THEN
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("Ada Lovelace");
        verify(userRepository, times(1)).save(user);
    }
}