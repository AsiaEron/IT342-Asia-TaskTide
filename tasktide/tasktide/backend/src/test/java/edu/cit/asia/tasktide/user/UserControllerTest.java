package edu.cit.asia.tasktide.user;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import edu.cit.asia.tasktide.shared.entity.UserModel;
import edu.cit.asia.tasktide.shared.repository.UserRepository;
import edu.cit.asia.tasktide.user.dto.UserSummaryDTO;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testGetAllUsers_Success() throws Exception {
        UserModel user1 = new UserModel();
        user1.setUser_id(1);
        user1.setFname("John");
        user1.setLname("Doe");
        user1.setEmail("john@example.com");

        UserModel user2 = new UserModel();
        user2.setUser_id(2);
        user2.setFname("Jane");
        user2.setLname("Smith");
        user2.setEmail("jane@example.com");

        List<UserModel> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        mockMvc.perform(get("/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].user_id").value(1))
                .andExpect(jsonPath("$[0].fname").value("John"))
                .andExpect(jsonPath("$[1].user_id").value(2))
                .andExpect(jsonPath("$[1].fname").value("Jane"));
    }
}