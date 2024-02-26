package com.simple.book.service.board;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.book.repository.BoardRepository;

@Service
public class BoardListSerivce {
	@Autowired
	private BoardRepository boardRepository;
	
	public HashMap<String, Object> boardList(){
		HashMap<String, Object> result = new HashMap<>();
		
		return result;
	}
}
