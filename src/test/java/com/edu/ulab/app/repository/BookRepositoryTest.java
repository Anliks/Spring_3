package com.edu.ulab.app.repository;

import com.edu.ulab.app.config.SystemJpaTest;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тесты репозитория {@link BookRepository}.
 */
@SystemJpaTest
public class BookRepositoryTest {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        SQLStatementCountValidator.reset();
    }

    @DisplayName("Сохранить книгу и автора. Число select должно равняться 2")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void findAllBadges_thenAssertDmlCount() {
        //Given

        Person person = new Person();
        person.setAge(111);
        person.setTitle("reader");
        person.setFullName("Test Test");

        Person savedPerson = userRepository.save(person);

        Book book = new Book();
        book.setAuthor("Test Author");
        book.setTitle("test");
        book.setPageCount(1000);
        book.setPerson(savedPerson);

        //When
        Book result = bookRepository.save(book);

        //Then
        assertThat(result.getPageCount()).isEqualTo(1000);
        assertThat(result.getTitle()).isEqualTo("test");
        assertSelectCount(2);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Обновить книгу и автора. Число select должно равняться 2")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void updateBadges_thenAssertDmlCount() {
        //Given
        Person givenPerson = userRepository.findById(1001L).orElseThrow();
        Book givenBook = bookRepository.findById(2002L).orElseThrow();

        //When
        givenPerson.setFullName("Maxim");
        givenPerson.setTitle("writer");
        givenPerson.setAge(12);

        Person updatedPerson = userRepository.save(givenPerson);

        givenBook.setPageCount(222);
        givenBook.setAuthor("Pushkin");
        givenBook.setTitle("Evgeniy Onegin");
        givenBook.setPerson(updatedPerson);

        Book updatedBook = bookRepository.save(givenBook);


        //Then
        assertThat(updatedPerson.getAge()).isEqualTo(12);
        assertThat(updatedBook.getAuthor()).isEqualTo("Pushkin");
        assertThat(updatedBook.getPageCount()).isEqualTo(222);
        assertSelectCount(2);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);


    }

    @DisplayName("Получить книгу и автора. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void getBadges_thenAssertDmlCount() {

        //When
        Book findBook = bookRepository.findById(2002L).orElseThrow();

        //Then
        assertThat(findBook.getPageCount()).isEqualTo(5500);
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Удалить книгу и автора. Число select должно равняться 2")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void deleteBadges_thenAssertDmlCount() {

        //Given


        //When
        bookRepository.deleteById(2002L);


        //Then
        assertThat(bookRepository.count()).isEqualTo(1);
        assertSelectCount(2);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(1);

    }
}
