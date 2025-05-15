package com.example.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

import java.util.List;

@Service
public class MessageService {
    //Instantiates MessageRepository and AccountRepository instances
    private MessageRepository messageRepository;
    private AccountRepository accountRepository;

    //Enables automatic dependency injection
    @Autowired
    //MessageService constructor
    public MessageService (MessageRepository messageRepository, AccountRepository accountRepository){
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }
    
    //Checks if a Message is valid
    public HttpStatus validMessage (Message message){
        /*If the message is not blank, greater than 255 characters, and the account posting the message exists, 
        the message is valid and the method returns a 200 Http status code*/
        if(!message.getMessageText().isBlank()
        && message.getMessageText().length() <= 255
        && accountRepository.findById(message.getPostedBy()).isPresent()){
            return HttpStatus.OK;
        }
        //Otherwise, it returns a 400 status code
        else return HttpStatus.valueOf(400);
    }

    /*Same but checks if the message is a valid update, the only difference being that this checks if the message 
    already exists instead of checking if the account posting it (postedBy) exists*/
    public HttpStatus validMessageUpdate (Integer messageId, String messageText){
        System.out.println(messageText);
        if(!messageText.isBlank()
        && messageText.length() <= 255
        && messageRepository.findById(messageId).isPresent()){
            return HttpStatus.OK;
        }
        else return HttpStatus.valueOf(400);
    }

    //Creates a message if the message is valid
    public Message createMessage(Message message){
        if(validMessage(message) == HttpStatus.OK){
            return messageRepository.save(message);
        }
        else return null;
    }

    //Returns all messages in the repository
    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    //Returns a message matching the provided messageId
    public Message getMessageByMessageId(Integer messageId){
        return messageRepository.findById(messageId).orElse(null);
    }

    //Returns true if the message exists, false if not
    public boolean messageExists(Integer messageId){
        if(messageRepository.existsById(messageId)){
            return true;
        }
        else return false;
    }

    //Deletes a message given a messageId
    public void deleteMessageByMessageId(Integer messageId){
        messageRepository.deleteById(messageId);
    }

    //Updates a message given a messageId, and text to update the message with
    public void updateMessageById(Integer messageId, String messageText){
        //Creates a message object to hold the message matching the messageId
        Message message = messageRepository.findById(messageId).orElse(null);
        //Updates the messageText field of the matching record to the messageText provided
        message.setMessageText(messageText);
    }

    //Gets a list of all messages matching the accountId (postedBy, in this case)
    public List<Message> getMessagesByAccountId(Integer accountId){
        return messageRepository.findByPostedBy(accountId).orElse(null);
    }
}
