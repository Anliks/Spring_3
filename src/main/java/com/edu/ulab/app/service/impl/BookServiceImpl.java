package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.BookService;
import liquibase.pro.packaged.A;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {


    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto createBook(BookDto bookDto) {
        Person person = userRepository.findById(bookDto.getUserId()).orElseThrow();
        Book book = bookMapper.bookDtoToBook(bookDto);
        book.setPerson(person);
        log.info("Mapped book: {}", book.getId(), book.getPageCount(), book.getPerson(), book.getAuthor(), book.getTitle());
        Book savedBook = bookRepository.save(book);
        log.info("Saved book: {}", savedBook);
        return bookMapper.bookToBookDto(savedBook);
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        Book book = bookMapper.bookDtoToBook(bookDto);
        book.setPerson(userRepository.findById(bookDto.getUserId()).get());
        log.info("get book {}", book);
        Book updateBook = bookRepository.save(book);
        log.info("update book {}", updateBook);
        return bookMapper.bookToBookDto(updateBook);
    }

    @Override
    public BookDto getBookById(Long id) {
        Book getBook = bookRepository.findById(id).orElseThrow();
        log.info("Get book by id: {}", getBook);
        return bookMapper.bookToBookDto(getBook);
    }

    @Override
    public void deleteBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow();
        log.info("Got Book for delete {}", book);
        bookRepository.deleteById(id);
    }

}
