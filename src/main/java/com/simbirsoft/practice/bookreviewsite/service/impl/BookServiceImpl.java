package com.simbirsoft.practice.bookreviewsite.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.simbirsoft.practice.bookreviewsite.dto.AddBookForm;
import com.simbirsoft.practice.bookreviewsite.dto.BookDTO;
import com.simbirsoft.practice.bookreviewsite.dto.CategoryDTO;
import com.simbirsoft.practice.bookreviewsite.entity.Book;
import com.simbirsoft.practice.bookreviewsite.entity.User;
import com.simbirsoft.practice.bookreviewsite.enums.BookStatus;
import com.simbirsoft.practice.bookreviewsite.exception.ResourceNotFoundException;
import com.simbirsoft.practice.bookreviewsite.exception.UserNotFoundException;
import com.simbirsoft.practice.bookreviewsite.repository.BookRepository;
import com.simbirsoft.practice.bookreviewsite.repository.CategoryRepository;
import com.simbirsoft.practice.bookreviewsite.repository.UsersRepository;
import com.simbirsoft.practice.bookreviewsite.service.BookService;
import org.apache.commons.io.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Roman Leontev
 * 03:32 23.06.2021
 * group 11-905
 */

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final CategoryRepository categoryRepository;

    private final UsersRepository usersRepository;

    private final ModelMapper modelMapper;

    private final Cloudinary cloudinary;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, CategoryRepository categoryRepository,
                           ModelMapper modelMapper, Cloudinary cloudinary, UsersRepository usersRepository) {

        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.cloudinary = cloudinary;
        this.usersRepository = usersRepository;
    }

    @Override
    public Page<BookDTO> findAllByBookStatus(Pageable pageable, BookStatus bookStatus) {
        return bookRepository.findAllByBookStatus(pageable, bookStatus)
                .map(book -> modelMapper.map(book, BookDTO.class));
    }

    @Override
    public Page<BookDTO> findAllByBookStatusAndTitle(Pageable pageable, BookStatus bookStatus, String title) {
        return bookRepository.findAllByBookStatusAndTitleContainingIgnoreCase(pageable, bookStatus, title)
                .map(book -> modelMapper.map(book, BookDTO.class));
    }

    @Override
    public int getBooksCountUserPushed(Long userId) {
        return bookRepository.countBookByPushedById(userId);
    }

    @Override
    public List<CategoryDTO> getAllBookCategory() {
        return categoryRepository.findAll().stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public BookDTO createNewBook(AddBookForm addBookForm, Long userId) {
        Optional<User> optionalUser = usersRepository.findById(userId);

        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException("User not found");
        }

        Book book = modelMapper.map(addBookForm, Book.class);

        book.setPushedBy(optionalUser.get());
        book.setBookStatus(BookStatus.PUBLIC); //TODO set moderation

        MultipartFile cover = addBookForm.getCover();
        if (cover != null) {
            try {
                File fileToUpload = new File(Objects.requireNonNull(cover.getOriginalFilename()));

                FileUtils.writeByteArrayToFile(fileToUpload, cover.getBytes());

                Map response = cloudinary.uploader().upload(fileToUpload, ObjectUtils.emptyMap());

                book.setCover((String) response.get("url"));

                fileToUpload.delete();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        book = bookRepository.save(book);

        return modelMapper.map(book, BookDTO.class);
    }

    @Override
    public Page<BookDTO> getAllUserBooks(Pageable pageable, Long userId) {
        return bookRepository.findAllByPushedById(pageable, userId)
                .map(book -> modelMapper.map(book, BookDTO.class));
    }

    @Override
    public boolean deleteUserBook(Long bookId, Long userId) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);

        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();

            if (userId.equals(book.getPushedBy().getId())) {
                bookRepository.delete(book);
                return true;
            }
        }

        return false;
    }

    @Override
    public BookDTO getById(Long id) {

        Book book = bookRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Book not found"));

        return modelMapper.map(book, BookDTO.class);
    }
}
