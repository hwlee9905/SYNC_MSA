package com.simple.book.domain.search.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.simple.book.domain.search.document.Search;

@Repository
public interface SearchRepository extends ElasticsearchRepository<Search, Long>{
	List<Search> findByDescriptionContaining(String keyword);

	List<Search> findByTitleContainingOrDescriptionContaining(String titleKeyword, String descriptionKeyword);

}
