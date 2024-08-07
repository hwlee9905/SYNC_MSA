package com.simple.book.domain.search.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.simple.book.domain.project.entity.Project;
import com.simple.book.domain.project.repository.ProjectRepository;
import com.simple.book.domain.search.document.Search;
import com.simple.book.domain.search.repository.SearchRepository;
import com.simple.book.global.advice.ResponseMessage;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {
	private final SearchRepository searchRepository;
	private final ProjectRepository projectRepository;
	
	@PostConstruct
	public void init() {
		List<Project> projects = projectRepository.findAll();
		List<Search> searchDocuments = projects.stream().map(project -> {
			Search doc = new Search();
			doc.setId(project.getId());
			doc.setTitle(project.getTitle());
			doc.setDescription(project.getDescription());
			return doc;
		}).collect(Collectors.toList());
		
		searchRepository.saveAll(searchDocuments);
	}
	
	public void newInit(Project project) {
		Search document = new Search();
		document.setId(project.getId());
		document.setTitle(project.getTitle());
		document.setDescription(project.getDescription());
		searchRepository.save(document);
	}
	
	public ResponseMessage search(String keyword) {
		List<Search> result = searchRepository.findByTitleContainingOrDescriptionContaining(keyword, keyword);
		return ResponseMessage.builder().value(result).build();
	}
}
