package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.UserDto;
import ru.practicum.models.User;
import ru.practicum.services.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(locations = "classpath:application.properties")
@Transactional
@SpringBootTest(classes = MainServerApp.class)
public class UsersTests {
    private final UserService userService;
    private final EntityManager em;

    @Test
    public void shouldCreateAndGetUser() {
        UserDto dto = new UserDto(null, "Ричард Брэнсон", "branson@spcetothemoon.com");
        dto = userService.create(dto);
        TypedQuery<User> userQuery = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = userQuery.setParameter("email", dto.getEmail()).getSingleResult();
        Assertions.assertNotNull(user);
        Assertions.assertEquals(user.getId(), dto.getId());
        Assertions.assertEquals(user.getName(), dto.getName());
    }
}