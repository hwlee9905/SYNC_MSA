package com.simple.book.domain.alarm.entity;

import java.util.UUID;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.simple.book.domain.alarm.dto.AlarmUrlDto;
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
import lombok.NoArgsConstructor;

@Entity
@Table(name = "alarm_url")
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmUrl extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "url", columnDefinition = "BINARY(16)", nullable = false)
	@JdbcTypeCode(SqlTypes.BINARY)
	private UUID url;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private User user;
	
	public AlarmUrlDto toDto() {
		return AlarmUrlDto.builder()
				.url(url)
				.user(user)
				.build();

	}
}
