package com.simple.book.domain.alarm.entity;

import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.simple.book.domain.user.entity.User;
import com.simple.book.global.util.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "alarm")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alarm extends BaseEntity{
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "alarm_id", columnDefinition = "BINARY(16)")
	@JdbcTypeCode(SqlTypes.BINARY)
	private UUID alarmId;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@Column(name = "message")
	private String message;
}
