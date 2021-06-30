package com.simbirsoft.practice.bookreviewsite.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.simbirsoft.practice.bookreviewsite.enums.Role;
import com.simbirsoft.practice.bookreviewsite.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "account")
public class User implements Serializable {

    private static final long serialVersionUID = 4437060923718907313L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name;

    @Column(length = 30, unique = true)
    private String email;

    private String hashedPassword;

    private String avatar;

    private String confirmCode;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Transient
    @JsonIgnore
    @OneToMany(mappedBy = "pushedBy", fetch = FetchType.EAGER)
    private Set<Book> books;

    @Transient
    @JsonIgnore
    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
    private Set<Review> reviews;

    @Transient
    @JsonIgnore
    @ManyToMany(mappedBy = "likedUsers", fetch = FetchType.EAGER)
    private Set<Book> favoriteBooks;
}
