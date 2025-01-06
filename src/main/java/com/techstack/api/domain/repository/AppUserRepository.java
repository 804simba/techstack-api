package com.techstack.api.domain.repository;

import com.techstack.api.domain.entity.AppUser;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    @Query("SELECT au FROM AppUser au WHERE au.email = :emailAddress OR au.phoneNumber = :phoneNumber")
    Optional<AppUser> findAppUserByEmailOrPhoneNumber(@Param("emailAddress") String emailAddress, @Param("phoneNumber") String phoneNumber);

    @Query("SELECT au FROM AppUser au WHERE au.id = :userId")
    Optional<AppUser> findAppUserById(@NotBlank(message = "UserId is required") Long userId);
}
