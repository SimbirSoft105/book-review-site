package com.simbirsoft.practice.bookreviewsite.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.simbirsoft.practice.bookreviewsite.dto.AddBookForm;
import com.simbirsoft.practice.bookreviewsite.dto.BookDTO;
import com.simbirsoft.practice.bookreviewsite.entity.Book;
import com.simbirsoft.practice.bookreviewsite.entity.Review;
import com.simbirsoft.practice.bookreviewsite.entity.User;
import com.simbirsoft.practice.bookreviewsite.enums.BookStatus;
import com.simbirsoft.practice.bookreviewsite.exception.ResourceNotFoundException;
import com.simbirsoft.practice.bookreviewsite.exception.UserNotFoundException;
import com.simbirsoft.practice.bookreviewsite.repository.BookRepository;
import com.simbirsoft.practice.bookreviewsite.repository.CategoryRepository;
import com.simbirsoft.practice.bookreviewsite.repository.UsersRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = BookServiceImpl.class)
@SpringBootTest
@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("BookServiceImpl is working when")
public class BookServiceImplTest {

    @Autowired
    private BookServiceImpl bookService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private UsersRepository usersRepository;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private Cloudinary cloudinary;

    @DisplayName("getById() is working")
    @Nested
    class ForGetById {

        private Long bookId = 1L;

        @Test
        void on_existing_book_return_BookDTO() {
            Book book = Book.builder()
                    .id(1L)
                    .build();

            when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

            BookDTO bookDTO = BookDTO.builder()
                    .id(bookId)
                    .build();

            Mockito.when(modelMapper.map(book, BookDTO.class)).thenReturn(bookDTO);

            assertEquals(bookDTO, bookService.getById(bookId));

            Mockito.verify(bookRepository, times(1)).findById(bookId);
            Mockito.verify(modelMapper, times(1)).map(book, BookDTO.class);
        }

        @Test
        void on_non_existing_book_throw_exception() {
            when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> bookService.getById(bookId));

            Mockito.verify(bookRepository, times(1)).findById(bookId);
            Mockito.verify(modelMapper, times(0)).map(any(Book.class), eq(BookDTO.class));
        }
    }

    @DisplayName("createNewBook() is working")
    @Nested
    class ForCreateNewBook {

        AddBookForm form = AddBookForm.builder()
                .title("Hello")
                .cover(new MockMultipartFile("file",
                        "hello.txt",
                        MediaType.TEXT_PLAIN_VALUE,
                        "Hello, World!".getBytes()))
                .build();

        Long userId = 1L;

        @Disabled
        @Test
        void on_positive_data_return_BookDTO() throws IOException {

            when(usersRepository.findById(userId)).thenReturn(Optional.of(
                    User.builder()
                            .id(userId)
                            .build()
            ));
            Book book = Book.builder()
                    .title(form.getTitle())
                    .build();
            when(modelMapper.map(form, Book.class)).thenReturn(book);

            File fileToUpload = new File(Objects.requireNonNull(form.getCover().getOriginalFilename()));

            when(cloudinary.uploader().upload(fileToUpload, ObjectUtils.emptyMap())).thenReturn(new HashMap<String, String>() {{
                put("url", "/newUrl");
            }});
            book.setCover("/newUrl");
            when(bookRepository.save(book)).thenReturn(book);
            when(modelMapper.map(book, BookDTO.class)).thenReturn(
                    BookDTO.builder()
                            .title(book.getTitle())
                            .cover(book.getCover())
                            .build()
            );
            assertNotNull(bookService.createNewBook(form, userId));
        }

        @Test
        void on_non_existing_user_throw_exception() {

            when(usersRepository.findById(userId)).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> bookService.createNewBook(form, userId));

            Mockito.verify(usersRepository, times(1)).findById(userId);
            Mockito.verify(modelMapper, times(0)).map(any(Book.class), eq(BookDTO.class));
        }
    }

    @DisplayName("getAllByBookStatus() is working")
    @Nested
    class ForGetAllByBookStatus {

        Book book1 = Book.builder().
                id(1L)
                .bookStatus(BookStatus.PUBLIC).
                        build();
        Book book2 = Book.builder().
                id(2L)
                .bookStatus(BookStatus.PUBLIC).
                        build();

        BookDTO bookDTO1 = BookDTO.builder().
                id(1L)
                .bookStatus(BookStatus.PUBLIC).
                        build();

        BookDTO bookDTO2 = BookDTO.builder().
                id(2L)
                .bookStatus(BookStatus.PUBLIC).
                        build();

        @BeforeEach
        public void setUp() {
            Mockito.when(modelMapper.map(book1, BookDTO.class)).thenReturn(bookDTO1);
            Mockito.when(modelMapper.map(book2, BookDTO.class)).thenReturn(bookDTO2);
        }

        @Test
        void on_positive_range_return_page_of_BookDTO() {
            Pageable pageableForTopBooks = PageRequest.of(0, 2, Sort.Direction.DESC, "rate");

            book1.setRate((float) 8.2);
            book2.setRate((float) 2.1);

            List<Book> books = Arrays.asList(
                    book1,
                    book2
            );

            Page<Book> booksPageForTopBooks = new PageImpl(books, pageableForTopBooks, books.size());

            bookDTO1.setRate((float) 8.2);
            bookDTO2.setRate((float) 2.1);

            List<BookDTO> bookDTOsForTopBooks = Arrays.asList(
                    bookDTO1,
                    bookDTO2
            );

            Page<BookDTO> bookDTOsPageForTopBooks = new PageImpl(bookDTOsForTopBooks, pageableForTopBooks, books.size());

            Mockito.when(bookRepository.findAllByBookStatus(pageableForTopBooks, BookStatus.PUBLIC)).thenReturn(booksPageForTopBooks);

            assertNotNull(bookService.getAllByBookStatus(pageableForTopBooks, BookStatus.PUBLIC));
            assertEquals(bookDTOsPageForTopBooks, bookService.getAllByBookStatus(pageableForTopBooks, BookStatus.PUBLIC));

            Mockito.verify(bookRepository, times(2)).findAllByBookStatus(pageableForTopBooks, BookStatus.PUBLIC);
            Mockito.verify(modelMapper, times(2)).map(book1, BookDTO.class);
            Mockito.verify(modelMapper, times(2)).map(book2, BookDTO.class);
        }

        @Test
        void on_negative_range_return_empty_content() {
            Pageable pageableForTopBooks = PageRequest.of(4, 2, Sort.Direction.DESC, "rate");

            List<Book> books = new ArrayList<>();

            Page<Book> booksPageForTopBooks = new PageImpl(books, pageableForTopBooks, books.size());

            List<BookDTO> bookDTOs = new ArrayList<>();

            Page<BookDTO> bookDTOsPageForTopBooks = new PageImpl(bookDTOs, pageableForTopBooks, books.size());

            Mockito.when(bookRepository.findAllByBookStatus(pageableForTopBooks, BookStatus.PUBLIC)).thenReturn(booksPageForTopBooks);

            assertEquals(bookDTOsPageForTopBooks, bookService.getAllByBookStatus(pageableForTopBooks, BookStatus.PUBLIC));

            Mockito.verify(bookRepository, times(1)).findAllByBookStatus(pageableForTopBooks, BookStatus.PUBLIC);
            Mockito.verify(modelMapper, times(0)).map(book1, BookDTO.class);
            Mockito.verify(modelMapper, times(0)).map(book2, BookDTO.class);
        }
    }

    @DisplayName("getAllByBookStatusAndTitle() is working")
    @Nested
    class ForGetAllByBookStatusAndTitle {

        Book book1 = Book.builder().
                id(1L)
                .bookStatus(BookStatus.PUBLIC).
                        build();
        Book book2 = Book.builder().
                id(2L)
                .bookStatus(BookStatus.PUBLIC).
                        build();

        BookDTO bookDTO1 = BookDTO.builder().
                id(1L)
                .bookStatus(BookStatus.PUBLIC).
                        build();

        BookDTO bookDTO2 = BookDTO.builder().
                id(2L)
                .bookStatus(BookStatus.PUBLIC).
                        build();

        @BeforeEach
        public void setUp() {
            Mockito.when(modelMapper.map(book1, BookDTO.class)).thenReturn(bookDTO1);
            Mockito.when(modelMapper.map(book2, BookDTO.class)).thenReturn(bookDTO2);
        }

        @Test
        void on_positive_range_return_page_of_BookDTO() {
            Pageable pageableForTopBooks = PageRequest.of(0, 2);

            String title = "World War";

            book1.setTitle(title + '1');
            book2.setTitle(title + '2');

            List<Book> books = Arrays.asList(
                    book1,
                    book2
            );

            Page<Book> booksPageForTopBooks = new PageImpl(books, pageableForTopBooks, books.size());

            bookDTO1.setTitle(title + '1');
            bookDTO2.setTitle(title + '2');

            List<BookDTO> bookDTOsForTopBooks = Arrays.asList(
                    bookDTO1,
                    bookDTO2
            );

            Page<BookDTO> bookDTOsPageForTopBooks = new PageImpl(bookDTOsForTopBooks, pageableForTopBooks, books.size());

            Mockito.when(bookRepository.findAllByBookStatusAndTitleContainingIgnoreCase(pageableForTopBooks, BookStatus.PUBLIC, title)).thenReturn(booksPageForTopBooks);

            assertNotNull(bookService.getAllByBookStatusAndTitle(pageableForTopBooks, BookStatus.PUBLIC, title));
            assertEquals(bookDTOsPageForTopBooks, bookService.getAllByBookStatusAndTitle(pageableForTopBooks, BookStatus.PUBLIC, title));

            Mockito.verify(bookRepository, times(2)).findAllByBookStatusAndTitleContainingIgnoreCase(pageableForTopBooks, BookStatus.PUBLIC, title);
            Mockito.verify(modelMapper, times(2)).map(book1, BookDTO.class);
            Mockito.verify(modelMapper, times(2)).map(book2, BookDTO.class);
        }

        @Test
        void on_negative_range_return_empty_content() {
            Pageable pageableForTopBooks = PageRequest.of(4, 2);

            String title = "World War";

            List<Book> books = new ArrayList<>();

            Page<Book> booksPageForTopBooks = new PageImpl(books, pageableForTopBooks, books.size());

            List<BookDTO> bookDTOs = new ArrayList<>();

            Page<BookDTO> bookDTOsPageForTopBooks = new PageImpl(bookDTOs, pageableForTopBooks, books.size());

            Mockito.when(bookRepository.findAllByBookStatusAndTitleContainingIgnoreCase(pageableForTopBooks, BookStatus.PUBLIC, title)).thenReturn(booksPageForTopBooks);

            assertEquals(bookDTOsPageForTopBooks, bookService.getAllByBookStatusAndTitle(pageableForTopBooks, BookStatus.PUBLIC, title));

            Mockito.verify(bookRepository, times(1)).findAllByBookStatusAndTitleContainingIgnoreCase(pageableForTopBooks, BookStatus.PUBLIC, title);
            Mockito.verify(modelMapper, times(0)).map(book1, BookDTO.class);
            Mockito.verify(modelMapper, times(0)).map(book2, BookDTO.class);
        }
    }

    @DisplayName("getAllByBookStatusAndSortByReviews() is working")
    @Nested
    class ForGetAllByBookStatusAndSortByReviews {

        Book book1 = Book.builder().
                id(1L)
                .bookStatus(BookStatus.PUBLIC).
                        build();
        Book book2 = Book.builder().
                id(2L)
                .bookStatus(BookStatus.PUBLIC).
                        build();

        BookDTO bookDTO1 = BookDTO.builder().
                id(1L)
                .bookStatus(BookStatus.PUBLIC).
                        build();

        BookDTO bookDTO2 = BookDTO.builder().
                id(2L)
                .bookStatus(BookStatus.PUBLIC).
                        build();

        @BeforeEach
        public void setUp() {
            Mockito.when(modelMapper.map(book1, BookDTO.class)).thenReturn(bookDTO1);
            Mockito.when(modelMapper.map(book2, BookDTO.class)).thenReturn(bookDTO2);
        }

        @Test
        void on_positive_range_return_page_of_BookDTO() {
            Pageable pageableForTopBooks = PageRequest.of(0, 2);

            Set<Review> setReviewsForBook1 = new HashSet<>();

            setReviewsForBook1.add(
                    Review.builder()
                    .id(1L)
                    .book(book1)
                    .build()
            );

            setReviewsForBook1.add(
                    Review.builder()
                            .id(2L).book(book1)
                            .build()
            );

            setReviewsForBook1.add(
                    Review.builder()
                            .id(3L)
                            .book(book1)
                            .build()
            );

            book1.setReviews(setReviewsForBook1);

            Set<Review> setReviewsForBook2 = new HashSet<>();

            setReviewsForBook2.add(
                    Review.builder()
                            .id(4L)
                            .book(book2)
                            .build()
            );

            book2.setReviews(setReviewsForBook2);

            List<Book> books = Arrays.asList(
                    book1,
                    book2
            );

            Page<Book> booksPageForTopBooks = new PageImpl(books, pageableForTopBooks, books.size());

            List<BookDTO> bookDTOsForTopBooks = Arrays.asList(
                    bookDTO1,
                    bookDTO2
            );

            Page<BookDTO> bookDTOsPageForTopBooks = new PageImpl(bookDTOsForTopBooks, pageableForTopBooks, books.size());

            Mockito.when(bookRepository.findAllByBookStatusOrderByReviews(pageableForTopBooks, BookStatus.PUBLIC)).thenReturn(booksPageForTopBooks);

            assertNotNull(bookService.getAllByBookStatusAndSortByReviews(pageableForTopBooks, BookStatus.PUBLIC));
            assertEquals(bookDTOsPageForTopBooks, bookService.getAllByBookStatusAndSortByReviews(pageableForTopBooks, BookStatus.PUBLIC));

            Mockito.verify(bookRepository, times(2)).findAllByBookStatusOrderByReviews(pageableForTopBooks, BookStatus.PUBLIC);
            Mockito.verify(modelMapper, times(2)).map(book1, BookDTO.class);
            Mockito.verify(modelMapper, times(2)).map(book2, BookDTO.class);
        }

        @Test
        void on_negative_range_return_empty_content() {
            Pageable pageableForTopBooks = PageRequest.of(4, 2);

            List<Book> books = new ArrayList<>();

            Page<Book> booksPageForTopBooks = new PageImpl(books, pageableForTopBooks, books.size());

            List<BookDTO> bookDTOs = new ArrayList<>();

            Page<BookDTO> bookDTOsPageForTopBooks = new PageImpl(bookDTOs, pageableForTopBooks, books.size());

            Mockito.when(bookRepository.findAllByBookStatusOrderByReviews(pageableForTopBooks, BookStatus.PUBLIC)).thenReturn(booksPageForTopBooks);

            assertEquals(bookDTOsPageForTopBooks, bookService.getAllByBookStatusAndSortByReviews(pageableForTopBooks, BookStatus.PUBLIC));

            Mockito.verify(bookRepository, times(1)).findAllByBookStatusOrderByReviews(pageableForTopBooks, BookStatus.PUBLIC);
            Mockito.verify(modelMapper, times(0)).map(book1, BookDTO.class);
            Mockito.verify(modelMapper, times(0)).map(book2, BookDTO.class);
        }
    }

    @DisplayName("getFirstByBookStatus() is working")
    @Nested
    class ForGetFirstByBookStatus {

        Book book1 = Book.builder().
                id(1L)
                .bookStatus(BookStatus.PUBLIC).
                        build();
        Book book2 = Book.builder().
                id(2L)
                .bookStatus(BookStatus.PUBLIC).
                        build();

        BookDTO bookDTO1 = BookDTO.builder().
                id(1L)
                .bookStatus(BookStatus.PUBLIC).
                        build();

        BookDTO bookDTO2 = BookDTO.builder().
                id(2L)
                .bookStatus(BookStatus.PUBLIC).
                        build();

        @BeforeEach
        public void setUp() {
            Mockito.when(modelMapper.map(book1, BookDTO.class)).thenReturn(bookDTO1);
        }

        @Test
        void on_not_null_result_return_BookDTO() {
            Mockito.when(bookRepository.findFirstByBookStatusOrderById(BookStatus.PUBLIC)).thenReturn(Optional.of(book1));

            assertEquals(bookDTO1, bookService.getFirstByBookStatus(BookStatus.PUBLIC));
            assertEquals(bookDTO1.getBookStatus(), bookService.getFirstByBookStatus(BookStatus.PUBLIC).getBookStatus());

            Mockito.verify(bookRepository, times(2)).findFirstByBookStatusOrderById(BookStatus.PUBLIC);
            Mockito.verify(modelMapper, times(2)).map(book1, BookDTO.class);
        }

        @Test
        void on_value_not_present_throw_exception() {
            Mockito.when(bookRepository.findFirstByBookStatusOrderById(BookStatus.PUBLIC)).thenReturn(Optional.empty());

            assertThrows(IllegalArgumentException.class, () -> bookService.getFirstByBookStatus(BookStatus.PUBLIC));

            Mockito.verify(bookRepository, times(1)).findFirstByBookStatusOrderById(BookStatus.PUBLIC);
            Mockito.verify(modelMapper, times(0)).map(any(Book.class), eq(BookDTO.class));
        }
    }
}