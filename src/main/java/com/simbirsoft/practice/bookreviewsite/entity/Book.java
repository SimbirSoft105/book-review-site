package com.simbirsoft.practice.bookreviewsite.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.simbirsoft.practice.bookreviewsite.enums.BookStatus;
import lombok.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@EqualsAndHashCode(exclude = {"pushedBy", "categories", "country", "language", "likedUsers"})
@ToString(exclude = {"pushedBy", "categories", "country", "language", "likedUsers"})
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 100, nullable = false)
    private String author;

    @Column(length = 1000, nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer releaseYear;

    private String cover;

    @Column(scale = 1, precision = 2, nullable = false)
    private float rate;

    @Enumerated(EnumType.STRING)
    private BookStatus bookStatus;

    @ManyToOne
    @JoinColumn(name = "country_id")
    @JsonIgnore
    private Country country;

    @ManyToOne
    @JoinColumn(name = "language_id")
    @JsonIgnore
    private Language language;

    @ManyToOne
    @JoinColumn(name = "pushed_by")
    @JsonIgnore
    private User pushedBy;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "book_category",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories;

    @JsonIgnore
    @OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
    private Set<Review> reviews;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "favorite_book",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> likedUsers;
}
