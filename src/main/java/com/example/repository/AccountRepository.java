package com.example.repository;
import java.util.Optional;
import com.example.entity.Account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>{
    //Stores an optional account (the result could be null, and optionals handle that) if an account is found possessing the given username
    Optional<Account> findByUsername(String username);
    //Stores an optional account (the result could be null, and optionals handle that) if an account is found possessing the given username AND password
    Optional<Account> findByUsernameAndPassword(String username, String password);
}
