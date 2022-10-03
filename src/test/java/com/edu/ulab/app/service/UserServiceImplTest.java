package com.edu.ulab.app.service;


import com.edu.ulab.app.dto.UserDto;

import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.edu.ulab.app.repository.BookRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Тестирование функционала {@link com.edu.ulab.app.service.impl.UserServiceImpl}.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing user functionality.")
public class UserServiceImplTest {
    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    BookRepository bookRepository;

    @Mock
    BookService bookService;

    @Mock
    UserMapper userMapper;


    private Person personInBase;

    @BeforeEach
    void setUp() {
        personInBase = new Person();
        personInBase.setId(1L);
        personInBase.setAge(55);
        personInBase.setFullName("Maxim");
        personInBase.setTitle("writer");
    }

    @Test
    @DisplayName("Создание пользователя. Должно пройти успешно.")
    void savePerson_Test() {
        //given

        UserDto userDto = new UserDto();
        userDto.setAge(11);
        userDto.setFullName("test name");
        userDto.setTitle("test title");

        Person person = new Person();
        person.setFullName("test name");
        person.setAge(11);
        person.setTitle("test title");

        Person savedPerson = new Person();
        savedPerson.setId(1L);
        savedPerson.setFullName("test name");
        savedPerson.setAge(11);
        savedPerson.setTitle("test title");

        UserDto result = new UserDto();
        result.setId(1L);
        result.setAge(11);
        result.setFullName("test name");
        result.setTitle("test title");


        //when

        when(userMapper.userDtoToPerson(userDto)).thenReturn(person);
        when(userRepository.save(person)).thenReturn(savedPerson);
        when(userMapper.personToUserDto(savedPerson)).thenReturn(result);


        //then

        UserDto userDtoResult = userService.createUser(userDto);
        assertEquals(1L, userDtoResult.getId());
    }

    @Test
    @DisplayName("Обновление пользователя. Должно пройти успешно.")
    void updatePerson_Test() {

        UserDto userDto = new UserDto();
        userDto.setAge(11);
        userDto.setFullName("test name");
        userDto.setTitle("test title");

        Person person = new Person();
        person.setFullName("test name");
        person.setAge(11);
        person.setTitle("test title");

        Person savedPerson = new Person();
        savedPerson.setId(1L);
        savedPerson.setFullName("test name");
        savedPerson.setAge(11);
        savedPerson.setTitle("test title");

        UserDto result = new UserDto();
        result.setId(1L);
        result.setAge(11);
        result.setFullName("test name");
        result.setTitle("test title");


        when(userMapper.userDtoToPerson(userDto)).thenReturn(person);
        when(userRepository.findById(person.getId())).thenReturn(Optional.ofNullable(personInBase));
        when(userRepository.save(person)).thenReturn(savedPerson);
        when(userMapper.personToUserDto(savedPerson)).thenReturn(result);

        UserDto userDtoResult = userService.updateUser(userDto);
        assertEquals(11, userDtoResult.getAge());
        assertEquals("test name", userDtoResult.getFullName());
        assertEquals("test title", userDtoResult.getTitle());

    }

    @Test
    @DisplayName("Получить пользователя. Должно пройти успешно.")
    void getPersonById_Test() {
        UserDto result = new UserDto();
        result.setId(1001L);
        result.setAge(99);
        result.setFullName("test user");
        result.setTitle("writer");

        when(userRepository.findById(1001L)).thenReturn(Optional.of(personInBase));
        when(userMapper.personToUserDto(personInBase)).thenReturn(result);

        UserDto userDtoResult = userService.getUserById(1001L);
        assertEquals("test user", userDtoResult.getFullName());
    }


    @Test
    @DisplayName("Полученние списка книг юзера. Должно пройти успешно.")
    void getUserBooks_Test() {
        List<Long> bookListId = List.of(11L, 22L);

        when(userRepository.getUserBooks(11L)).thenReturn(bookListId);

        List<Long> userBooks = userService.getUserBooks(11L);
        assertEquals(bookListId, userBooks);

    }

    @Test
    @DisplayName("Удаление юзера. Должно пройти успешно.")
    void deletePersonById_Test() {
        //given
        personInBase.setId(1001L);
        personInBase.setAge(55);
        personInBase.setFullName("default uer");
        personInBase.setTitle("reader");


        //when
        when(userRepository.findById(1001L)).thenReturn(Optional.of(personInBase));

        //then
        userService.deleteUserById(1001L);
        assertEquals(0, userRepository.count());
    }

    @Test
    @DisplayName("!!Исключение. Получить пользователя по id")
    public void whenExceptionThrown_thenAssertionSucceeds() {
        Exception exception = assertThrows(NotFoundException.class, () -> {
            userService.getUserById(200L);
        });

        String expectedMessage = "User not found by userId: 200";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
