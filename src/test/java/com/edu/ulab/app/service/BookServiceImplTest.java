package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тестирование функционала {@link com.edu.ulab.app.service.impl.BookServiceImpl}.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing book functionality.")
public class BookServiceImplTest {
    @InjectMocks
    BookServiceImpl bookService;

    @Mock
    BookRepository bookRepository;

    @Mock
    BookMapper bookMapper;

    @Mock
    UserRepository userRepository;


    private Person person;
    private Book book;
    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        person = new Person();
        person.setId(1L);

        bookDto = new BookDto();

        bookDto.setPageCount(1000);
        bookDto.setAuthor("test author");
        bookDto.setTitle("test title");
        bookDto.setUserId(1L);

        book = new Book();
        book.setTitle("test title");
        book.setAuthor("test author");
        book.setPageCount(1000);
        book.setPerson(person);

    }


    @Test
    @DisplayName("Создание книги. Должно пройти успешно.")
    void saveBook_Test() {
        //given

        BookDto result = new BookDto();
        result.setId(1L);
        result.setUserId(1L);
        result.setAuthor("test author");
        result.setTitle("test title");
        result.setPageCount(1000);

        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setPageCount(1000);
        savedBook.setTitle("test title");
        savedBook.setAuthor("test author");
        savedBook.setPerson(person);

        //when


        when(bookMapper.bookDtoToBook(bookDto)).thenReturn(book);
        when(userRepository.findById(1L)).thenReturn(Optional.of(person));
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.bookToBookDto(savedBook)).thenReturn(result);

        //then
        BookDto bookDtoResult = bookService.createBook(bookDto);
        assertEquals(1L, bookDtoResult.getId());
    }

    @Test
    @DisplayName("Обновление книги. Должно пройти успешно.")
    void updateBook_Test() {

        //Given
        Book updateBook = new Book();
        updateBook.setId(1L);
        updateBook.setAuthor("on more author");
        updateBook.setTitle("more default book");
        updateBook.setPageCount(665);
        updateBook.setPerson(person);

        BookDto result = new BookDto();
        result.setId(1L);
        result.setUserId(1L);
        result.setAuthor("on more author");
        result.setTitle("more default book");
        result.setPageCount(665);


        //When

        when(bookMapper.bookDtoToBook(bookDto)).thenReturn(book);
        when(userRepository.findById(1L)).thenReturn(Optional.of(person));
        when(bookRepository.save(book)).thenReturn(updateBook);
        when(bookMapper.bookToBookDto(updateBook)).thenReturn(result);

        //Then
        BookDto givenBookDto = bookService.updateBook(bookDto);
        assertEquals(665, givenBookDto.getPageCount());

    }


    @Test
    @DisplayName("Плучение книги. Должно пройти успешно.")
    void getBookById_Test() {

        Book inBaseBook = new Book();
        inBaseBook.setId(1L);
        inBaseBook.setAuthor("Pushkin");
        inBaseBook.setTitle("Evgeniy Onegin");
        inBaseBook.setPageCount(135);
        inBaseBook.setPerson(person);

        BookDto inBaseBookDto = new BookDto();
        inBaseBookDto.setId(1L);
        inBaseBookDto.setAuthor("Pushkin");
        inBaseBookDto.setTitle("Evgeniy Onegin");
        inBaseBookDto.setPageCount(135);
        inBaseBookDto.setUserId(1L);


        //When
        when(bookRepository.findById(1L)).thenReturn(Optional.of(inBaseBook));
        when(bookMapper.bookToBookDto(inBaseBook)).thenReturn(inBaseBookDto);

        //Then
        BookDto result = bookService.getBookById(1L);

        assertEquals("Evgeniy Onegin", result.getTitle());
        assertEquals(135, result.getPageCount());


    }

    @Test
    @DisplayName("Удаление книги. Должно пройти успешно.")
    void deleteBookById_Test() {
        //Given
        Book inBaseBook = new Book();
        inBaseBook.setId(1L);
        inBaseBook.setAuthor("Pushkin");
        inBaseBook.setTitle("Evgeniy Onegin");
        inBaseBook.setPageCount(135);
        inBaseBook.setPerson(person);

        //When
        when(bookRepository.findById(1L)).thenReturn(Optional.of(inBaseBook));

        //Then
        bookService.deleteBookById(1L);

    }
}
