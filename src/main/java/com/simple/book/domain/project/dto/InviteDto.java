package com.simple.book.domain.project.dto;

import java.util.UUID;

import com.simple.book.domain.project.entity.Invite;
import com.simple.book.domain.project.entity.Project;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InviteDto {
	private String url;
	private Project project;
	private UUID token;
	
	@Builder
	public InviteDto (String url, Project project, UUID token) {
		this.url = url;
		this.project = project;
		this.token=token;
	}
	
	public Invite toEntity() {
		return Invite.builder()
				.url(url)
				.project(project)
				.token(token)
				.build();
	}
}
