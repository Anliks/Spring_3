package com.edu.ulab.app.repository;

import com.edu.ulab.app.config.SystemJpaTest;
import com.edu.ulab.app.entity.Person;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import liquibase.pro.packaged.P;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тесты репозитория {@link UserRepository}.
 */
@SystemJpaTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        SQLStatementCountValidator.reset();
    }

    @DisplayName("Сохранить юзера. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void insertPerson_thenAssertDmlCount() {
        //Given
        Person person = new Person();
        person.setAge(111);
        person.setTitle("writer");
        person.setFullName("Test Test");

        //When
        Person result = userRepository.save(person);

        //Then
        assertThat(result.getAge()).isEqualTo(111);
        assertThat(result.getTitle()).isEqualTo("writer");
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Обновление юзера. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void updatePerson_thenAssertDmlCount() {


        //Given
        Person updatedPerson = userRepository.findById(1001L).orElseThrow();

        //When
        updatedPerson.setFullName("Maxim");
        updatedPerson.setTitle("writer");
        updatedPerson.setAge(22);
        Person result = userRepository.save(updatedPerson);

        //Then
        assertThat(result.getAge()).isEqualTo(22);
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);

    }

    @DisplayName("Получение юзера. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void getPerson_thenAssertDmlCount() {

        //When
        Person result = userRepository.findById(1001L).orElseThrow();

        //Then
        assertThat(result.getAge()).isEqualTo(55);
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);


    }

    @DisplayName("Удаление юзера. Число select должно равняться 5")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void deletePerson_thenAssertDmlCount() {
        //When
        List<Long> userBooks = userRepository.getUserBooks(1001L);
        userBooks.forEach(bookRepository::deleteById);
        userRepository.deleteById(1001L);

        //Then
        assertThat(userRepository.count()).isEqualTo(0);
        assertSelectCount(5);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(3);


    }
}
