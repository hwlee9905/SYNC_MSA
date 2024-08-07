package com.simple.book.domain.search.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.Data;

@Document(indexName = "search")
@Data
public class Search {
	@Id
	private long id;
	private String title;
	private String description;
}
