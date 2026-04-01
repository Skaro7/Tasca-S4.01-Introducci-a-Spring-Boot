package userapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import userapi.controllers.UserController;
import userapi.models.User;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        UserController.users.clear();
    }

    @Test
    void getUsers_returnsEmptyListInitially() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void createUser_returnsUserWithId() throws Exception {
        String json = """
                {
                  "name": "Ada Lovelace",
                  "email": "ada@example.com"
                }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Ada Lovelace"))
                .andExpect(jsonPath("$.email").value("ada@example.com"));
    }

    @Test
    void getUserById_returnsCorrectUser() throws Exception {
        String json = """
                {
                  "name": "Ada Lovelace",
                  "email": "ada@example.com"
                }
                """;

        MvcResult result = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        User created = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

        mockMvc.perform(get("/users/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ada Lovelace"));
    }

    @Test
    void getUserById_returnsNotFoundIfMissing() throws Exception {
        mockMvc.perform(get("/users/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUsers_withNameParam_returnsFilteredUsers() throws Exception {
        String ada = """
                {"name": "Ada Lovelace", "email": "ada@example.com"}
                """;
        String john = """
                {"name": "John Doe", "email": "john@example.com"}
                """;

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(ada));
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(john));

        mockMvc.perform(get("/users?name=ada"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Ada Lovelace"));
    }
}