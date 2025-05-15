package com.example.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;


@Service
public class AccountService {
    //Instantiates an instance of accountRepository
    private AccountRepository accountRepository;

     //Enables automatic dependency injection
    @Autowired
    //AccountService Constructor
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    //Valid account registration credential check
    public HttpStatus validRegister(Account account){
        //If the username already exists, return a 409 Http status code
        if(accountRepository.findByUsername(account.getUsername()).isPresent()){
            return HttpStatus.valueOf(409);
        }
        //If the username is null, blank, or full of whitespace (isBlank handles all of these) or the password less than 4 characters, returns a 400 Http status code
        if(account.getUsername().isBlank()
        || account.getPassword().length() < 4){
            return HttpStatus.valueOf(400);
        }
        //If neither is the case--meaning the credentials are valid, return a 200 Http status code (OK)
        else return HttpStatus.OK;
    }
    
    //Account registration method. Saves an account to the repository
    public Account register(Account account){
        return accountRepository.save(account);
    }

    //Login method. Leverages findByUsernameAndPassword method and returns the result
    public Account login(Account account){
        return accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword()).orElse(null);
    }
}
