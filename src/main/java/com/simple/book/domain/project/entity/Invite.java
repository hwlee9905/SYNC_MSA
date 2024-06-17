package com.simple.book.domain.project.entity;

import java.util.UUID;

import com.simple.book.domain.project.dto.InviteDto;
import com.simple.book.global.util.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "invite")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invite extends BaseEntity {
	@Id
	@Column(name = "url", nullable = false)
	private String url;
	
	@ManyToOne
	@JoinColumn(name = "project_id", nullable = false)
	private Project project;
	
	@Column(name = "token", nullable = false)
	private UUID token;
	
	public InviteDto toDto() {
		return InviteDto.builder()
				.url(url)
				.project(project)
				.token(token)
				.build();
	}
	
}
