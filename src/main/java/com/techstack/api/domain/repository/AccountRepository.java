package com.techstack.api.domain.repository;

import com.techstack.api.domain.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Account a WHERE a.accountNumber = :accountNumber")
    boolean existsByAccountNumber(@Param("accountNumber") String accountNumber);

    @Query("SELECT a FROM Account a WHERE a.user.id = :userId")
    Page<Account> findAccountsByUserId(Long userId, Pageable pageable);

    @Query("SELECT a FROM Account a " +
            "JOIN a.product p " +
            "WHERE a.name LIKE CONCAT('%',:keyword,'%') " +
            "OR a.accountNumber LIKE CONCAT('%',:keyword,'%') " +
            "OR a.type LIKE CONCAT('%',:keyword,'%') " +
            "OR p.name LIKE CONCAT('%',:keyword,'%') " +
            "OR p.currency LIKE CONCAT('%',:keyword,'%') " +
            "ORDER BY a.id DESC")
    List<Account> searchAccounts(String keyword);
}
