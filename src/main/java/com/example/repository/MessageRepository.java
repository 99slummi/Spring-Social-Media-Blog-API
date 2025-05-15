package com.example.repository;
import com.example.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer>{
    //Stores an optional List of type Message (Optional because the search could be null) if there is a Message found that is postedBy the given postedBy (accountId)
    Optional<List<Message>> findByPostedBy(Integer postedBy);
}

