package com.simple.book.domain.alarm.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.simple.book.domain.alarm.dto.TopicDto;
import com.simple.book.global.util.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity(name = "topic")
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Topic extends BaseEntity{
	@Id
	@Column(name = "name")
	private String name;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "id")
	private long id;

	public TopicDto toDto() {
		return TopicDto.builder()
				.name(name)
				.type(type)
				.id(id)
				.build();
	}
	
}
