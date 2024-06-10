package com.simple.book.domain.alarm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simple.book.domain.alarm.entity.Alarm;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long>{

//	int deleteByName(String topicname);
//
//	List<Alarm> findByType(String type);

}
