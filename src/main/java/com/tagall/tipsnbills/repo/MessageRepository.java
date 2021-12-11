package com.tagall.tipsnbills.repo;

import com.tagall.tipsnbills.module.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
