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
import com.simbirsoft.practice.bookreviewsite.repository.ReviewsRepository;
import com.simbirsoft.practice.bookreviewsite.repository.UsersRepository;
import com.simbirsoft.practice.bookreviewsite.service.BookService;
import com.simbirsoft.practice.bookreviewsite.service.ReviewsService;
import org.apache.commons.io.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    private final ReviewsRepository reviewsRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, CategoryRepository categoryRepository,
                           ModelMapper modelMapper, Cloudinary cloudinary,
                           UsersRepository usersRepository, ReviewsRepository reviewsRepository) {

        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.cloudinary = cloudinary;
        this.usersRepository = usersRepository;
        this.reviewsRepository = reviewsRepository;
    }

    @Override
    public Page<BookDTO> getAllByBookStatus(Pageable pageable, BookStatus bookStatus) {
        return bookRepository.findAllByBookStatus(pageable, bookStatus)
                .map(book -> modelMapper.map(book, BookDTO.class));
    }

    @Override
    public Page<BookDTO> getAllByBookStatusAndTitle(Pageable pageable, BookStatus bookStatus, String title) {
        return bookRepository.findAllByBookStatusAndTitleContainingIgnoreCase(pageable, bookStatus, title)
                .map(book -> modelMapper.map(book, BookDTO.class));
    }

    @Override
    public Page<BookDTO> getAllByBookStatusAndSortByReviews(Pageable pageable, BookStatus bookStatus) {
        return bookRepository.findAllByBookStatusWithSortByReview(pageable, bookStatus).map(book -> modelMapper.map(book, BookDTO.class));
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
        if (!cover.isEmpty()) {
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

                try {
                    String cover = book.getCover();
                    String publicId = cover.substring(
                            cover.lastIndexOf("/") + 1, cover.lastIndexOf("."));

                    cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                } catch (IOException ignore) {}

                bookRepository.delete(book);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean deleteUserFavoriteBook(Long bookId, Long userId) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        Optional<User> optionalUser = usersRepository.findById(userId);

        if (optionalBook.isPresent() &&  optionalUser.isPresent()) {
            Book book = optionalBook.get();
            User user = optionalUser.get();

            user.getFavoriteBooks().remove(book);
            book.getLikedUsers().remove(user);

            return true;
        }

        return false;
    }

    @Override
    public BookDTO getFirstByBookStatus(BookStatus bookStatus) {
        return modelMapper.map(bookRepository.findFirstByBookStatusOrderById(bookStatus).orElseThrow(IllegalArgumentException::new), BookDTO.class);
    }

    @Override
    public Page<BookDTO> getTopByBookStatus(Pageable pageable, BookStatus bookStatus) {
        return bookRepository.findAllByBookStatus(pageable, bookStatus).map(book -> modelMapper.map(book, BookDTO.class));
    }

    @Override
    public BookDTO getById(Long id) {

        Book book = bookRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Book not found"));

        return modelMapper.map(book, BookDTO.class);
    }

    @Override
    public float recalculateBookRate(Long bookId) {

        List<Review> reviews = reviewsRepository.getAllByBookId(bookId);

        float sum = 0;
        for (Review review: reviews) {
            sum += review.getMark();
        }
        float rate = sum / reviews.size();

        bookRepository.recalculateBookRate(rate, bookId);

        return rate;
    }

    @Override
    public Page<BookDTO> getUserFavoriteBooks(Pageable pageable, Long userId) {

        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return bookRepository.findAllByLikedUsersIsContaining(pageable, user)
                .map(book -> modelMapper.map(book, BookDTO.class));
    }
}
