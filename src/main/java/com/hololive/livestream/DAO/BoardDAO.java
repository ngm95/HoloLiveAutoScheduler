package com.hololive.livestream.DAO;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hololive.livestream.DTO.BoardDTO;
import com.hololive.livestream.util.BoardMinMax;

@Repository
public class BoardDAO {
	private static final String MAPPER = "BoardMapper";

	@Autowired
	SqlSessionTemplate template;
	
	public void create(BoardDTO boardDto) {
		template.insert(MAPPER + ".create", boardDto);
	}
	
	public BoardDTO readByBoardId(int boardId) {
		return template.selectOne(MAPPER + ".readByBoardId", boardId);
	}
	
	public int readBoardId(BoardDTO boardDto) {
		return template.selectOne(MAPPER + ".readBoardId", boardDto);
	}
	
	public List<BoardDTO> readLimitList(BoardMinMax bmm) {
		return template.selectList(MAPPER + ".readLimitList", bmm);
	}
	
	public List<BoardDTO> readAllLikeTitle(String title) {
		return template.selectList(MAPPER + ".readAllLikeTitle", title);
	}
	
	public int readMaxCount() {
		return template.selectOne(MAPPER + ".readMaxCount");
	}
	
	public void increaseLike(int boardId) {
		template.update(MAPPER + ".increaseLike", boardId);
	}
	
	public void decreaseLike(int boardId) {
		template.update(MAPPER + ".decreaseLike", boardId);
	}
	
	public void increaseDislike(int boardId) {
		template.update(MAPPER + ".increaseDislike", boardId);
	}
	
	public void decreaseDislike(int boardId) {
		template.update(MAPPER + ".decreaseDislike", boardId);
	}
	
	public void increaseViewed(int boardId) {
		template.update(MAPPER + ".increaseViewed", boardId);
	}
}
