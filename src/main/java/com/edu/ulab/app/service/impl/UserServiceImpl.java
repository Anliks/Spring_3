package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.exception.NotValidDataException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.validation.UserValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BookRepository bookRepository;



    @Override
    public UserDto createUser(UserDto userDto) {
        Person user = userMapper.userDtoToPerson(userDto);
        log.info("Mapped user: {}", user);
        Person savedUser = userRepository.save(user);
        log.info("Saved user: {}", savedUser);
        return userMapper.personToUserDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        if (UserValidation.isValid(userDto)) {
            Person user = userMapper.userDtoToPerson(userDto);
            Person findUser = userRepository.findById(userDto.getId()).orElseThrow();
            log.info("find user for update {}", findUser);
            user.setId(findUser.getId());
            Person updatedUser = userRepository.save(user);
            log.info("user update {}", updatedUser);
            return userMapper.personToUserDto(updatedUser);
        } else {
            throw new NotValidDataException("Not Valid Data");
        }
    }

    @Override
    public UserDto getUserById(Long id) {
        log.info("Got ID for get User {}", id);
        UserDto userDto = userMapper.personToUserDto(userRepository.findById(id).orElseThrow(() -> new NotFoundException("user ID Not found " + id)));
        log.info("Got User By ID {}", userDto);
        return userDto;
    }

    @Override
    public void deleteUserById(Long id) {
            List<Long> bookIdList = userRepository.getUserBooks(id);
            bookIdList.stream().filter(Objects::nonNull)
                    .forEach(bookRepository::deleteById);
            userRepository.deleteById(id);
            log.info("User deleted");

    }
    public List<Long> getUserBooks(Long id) {
        log.info("Get List books {}", id);
        return userRepository.getUserBooks(id);
    }
}
