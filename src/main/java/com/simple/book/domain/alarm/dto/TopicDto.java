package com.simple.book.domain.alarm.dto;

import com.simple.book.domain.alarm.entity.Topic;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TopicDto {
	private String name;
	private String type;
	private long id;
	
	@Builder
	public TopicDto(String name, String type, long id) {
		this.name=name;
		this.type=type;
		this.id=id;
	}
	
	public Topic toEntity() {
		return Topic.builder()
				.name(name)
				.type(type)
				.id(id)
				.build();
	}
	
}
