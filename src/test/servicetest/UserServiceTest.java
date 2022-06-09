
import com.example.application.backend.data.entity.Department;
import com.example.application.backend.data.entity.Tracking;
import com.example.application.backend.data.entity.User;
import com.example.application.backend.data.repository.UserRepository;
import com.example.application.backend.data.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;

@SpringBootTest(classes = UserServiceTest.class)
public class UserServiceTest {

    private static final Long ID = 1L;

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    private User givenUser() {
        Tracking tracking = new Tracking();
        List<Tracking> partes = new ArrayList<>();
        partes.add(tracking);
        return User.builder()
                .username("test")
                .email("test@test.com")
                .department(Department.IT)
                .name("Juan")
                .surname("Santos")
                .trackingList(partes)
                .build();
    }

    @Test
    public void should_save_user() {
        //Given
        User user = givenUser();
        //When
        Mockito.when(repository.save(Mockito.any(User.class))).thenReturn(user);
        //Then
        assertEquals(user, service.update(user));
    }

    @Test
    public void should_find_all_users() {
        //Given
        User user = givenUser();
        //When
        Mockito.when(repository.findAll()).thenReturn(List.of(user));
        //Then
        assertEquals(List.of(user), service.findAll());
    }

    @Test
    public void should_find_user_by_id() {
        //Given
        User user = givenUser();
        //When
        Mockito.when(repository.findById(ID)).thenReturn(Optional.of(user));
        //Then
        assertEquals(Optional.of(user), service.findById(ID));
    }
    @Test
    public void should_delete_user_by_id() {
        //Given
        User user = givenUser();
        Mockito.when(repository.findById(ID)).thenReturn(Optional.of(user));
        //When
        doNothing().when(repository).deleteById(ID);
        //Then
        assertDoesNotThrow(() -> service.deleteById(ID));
    }
}
