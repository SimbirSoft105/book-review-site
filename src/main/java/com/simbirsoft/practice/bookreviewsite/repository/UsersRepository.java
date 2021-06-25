package com.simbirsoft.practice.bookreviewsite.repository;

import com.simbirsoft.practice.bookreviewsite.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {

    Optional<User> getUserByConfirmCode(String confirmCode);

    Optional<User> getByEmail(String email);

    boolean existsByEmail(String email);

    @Transactional
    @Modifying
    @Query("update User user set user.name = :name, user.email = :email, user.avatar = :avatar")
    void editProfile(@Param("name") String name,
                     @Param("email") String email,
                     @Param("avatar") String avatar);

}
