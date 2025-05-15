package com.example.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.service.AccountService;
import com.example.service.MessageService;
import com.example.entity.Account;
import com.example.entity.Message;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

//Declares this class as a RestController allowing default RESTful API implementations
@RestController
//Indicates that all methods of this class should be returned as the response body of a JSON Object
@ResponseBody
public class SocialMediaController {
    //Instantiates AccountService and MessageService
    private AccountService accountService;
    private MessageService messageService;

    //SocialMediaController class constructor
    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }

    //Creates an HTTP POST endpoint at the URI /register
    @PostMapping("/register")
    //Reads the Request Body as an Account object
    public ResponseEntity<Account> register(@RequestBody Account newAccount){
        //Creates a new account if the registration is valid and returns a 200 Http status code
        if(accountService.validRegister(newAccount) == HttpStatus.OK){
            Account registeredAccount = accountService.register(newAccount);
            return new ResponseEntity<>(registeredAccount, HttpStatus.OK);
        }
        /*Otherwise, returns the result of the validRegister method, which will be a 409 Http status code if the
        registration is invalid due to the username already existing, or a 400 Http status if the registration is
        invalid for any other reason*/
        else{
            return new ResponseEntity<> (newAccount, accountService.validRegister(newAccount));
        }

    }

    //Creates an HTTP POST endpoint at the URI /login
    @PostMapping("/login")
    //Reads the Request Body as an Account object
    public ResponseEntity<Account> login (@RequestBody Account account){
        /*If the accountService login method is not null (meaning it passes all its checks), retrieves the account matching the
        credentials, and returns a 200 Http status code*/
        if(accountService.login(account) != null){
            return ResponseEntity.status(200).body(accountService.login(account));
        }
        //Otherwise, returns a 401 Http status (Unauthorized)
        else return ResponseEntity.status(401).body(account);
    }

    //Creates an HTTP POST endpoint at the URI /messages
    @PostMapping("/messages")
    //Reads the Request Body as a Message object
    public ResponseEntity<Message> createMessage (@RequestBody Message message){
        //If the messageService createMessage method is not null, creates a new message and returns a 200 Http status code
        if(messageService.createMessage(message) != null){
            return ResponseEntity.status(200).body(messageService.createMessage(message));
        }
        //Otherwise, returns a 400 Http status code
        else return ResponseEntity.status(400).body(message);
    }

    //Creates an HTTP GET endpoint at the URI /messages
    @GetMapping("/messages")
    //Returns all messages in the message repository
    public ResponseEntity<List<Message>> getAllMessages(){
        return ResponseEntity.status(200).body(messageService.getAllMessages());
    }

    //Creates an HTTP GET endpoint at the URI /messages/{messageId}
    @GetMapping("/messages/{messageId}")
    //Extracts the path variable {messageId} as an integer, and uses it as the argument in the getMessageById method
    public ResponseEntity<Message> getMessageByMessageId(@PathVariable Integer messageId){
        return ResponseEntity.status(200).body(messageService.getMessageByMessageId(messageId));
    }

    //Creates an HTTP DELETE endpoint at the URI /messages/{messageId}
    @DeleteMapping("/messages/{messageId}")
    //Extracts the path variable {messageId} as an integer, and uses it as the argument in the deleteMessageByID method
    public ResponseEntity<Integer> deleteMessageByMessageId(@PathVariable Integer messageId){
        //Checks if the message exists, deletes it if it does, and returns 1, indicating the amount of records affected
        if(messageService.messageExists(messageId)){
            messageService.deleteMessageByMessageId(messageId);
            return ResponseEntity.status(200).body(1);
        }
        //If the message doesnt exist, it will still return the 200 Http status code, but just return an empty JSON instead
        else
            return ResponseEntity.status(200).build();
    }

    //Creates an HTTP PATCH endpoint at the URI /messages/{messageId}
    @PatchMapping("/messages/{messageId}")
    /*Extracts the path variable {messageId} as an integer, and uses it as the argument in the validMessageUpdate and 
    updateMessageById methods, and reads the request body as a Message object. Then, it leverages the Message object's
    getter method to get the message text from the response body as a String that can be passed to the validMessageUpdate
    and updateMessageById methods*/
    public ResponseEntity<Integer> updateMessageById(@PathVariable Integer messageId, @RequestBody Message message){
        //If the message update is valid, updates the message and returns 1, indicating the amound of records affected
        if(messageService.validMessageUpdate(messageId, message.getMessageText()) == HttpStatus.OK){
            messageService.updateMessageById(messageId, message.getMessageText());
            return ResponseEntity.status(200).body(1);
        }
        //Otherwise, returns a 400 Http status code and an empty JSON response body
        else return ResponseEntity.status(400).build();
    }

    //Creates an HTTP GET endpoint at the URI /accounts/{accountId}/messages
    @GetMapping("/accounts/{accountId}/messages")
    //Extracts the path variable {accountId} as an integer, and uses it as the argument in the getMessagesByAccountID method
    public ResponseEntity<List<Message>> getMessagesByAccountId(@PathVariable Integer accountId){
        //Returns a 200 Http status code and a list of all messages matching that accountId
        return ResponseEntity.status(200).body(messageService.getMessagesByAccountId(accountId));
    }

}
