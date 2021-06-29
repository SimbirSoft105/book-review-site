package com.simbirsoft.practice.bookreviewsite.entity;

import com.simbirsoft.practice.bookreviewsite.enums.BookStatus;
import com.simbirsoft.practice.bookreviewsite.enums.Country;
import com.simbirsoft.practice.bookreviewsite.enums.Language;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
<<<<<<< Updated upstream
@EqualsAndHashCode(exclude = {"pushedBy", "categories"})
=======
@EqualsAndHashCode(exclude = {"pushedBy", "categories", "country", "language", "reviews"})
@ToString(exclude = {"pushedBy", "categories", "country", "language", "reviews"})
>>>>>>> Stashed changes
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private Country country;

    @Enumerated(EnumType.STRING)
    private Language language;

    @Column(length = 100, nullable = false)
    private String author;

    @Column(length = 1000, nullable = false)
    private String description;

    @Column(length = 4, nullable = false)
    private String releaseYear;

    private String cover;

    @Column(scale = 1, precision = 2, nullable = false)
    private float rate;

    @Enumerated(EnumType.STRING)
    private BookStatus bookStatus;

    @ManyToOne
    @JoinColumn(name = "pushed_by")
    private User pushedBy;

    @ManyToMany
    @JoinTable(name = "book_category",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories;

}
