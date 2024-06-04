package com.simple.book.domain.alarm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simple.book.domain.alarm.entity.Topic;

@Repository
public interface TopicRepository extends JpaRepository<Topic, String>{

	int deleteByName(String topicname);

}
