package com.hololive.livestream.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hololive.livestream.DAO.BoardDAO;
import com.hololive.livestream.DTO.BoardDTO;

@Service
public class BoardService {
	@Autowired
	BoardDAO boardDao;
	
	public void create(BoardDTO boardDto) {
		boardDao.create(boardDto);
	}
	
	public List<BoardDTO> readAllBoard() {
		return boardDao.readAllBoard();
	}
	
	public int readBoardId(BoardDTO boardDto) {
		return boardDao.readBoardId(boardDto);
	}
	
	public BoardDTO readByBoardId(int boardId) {
		return boardDao.readByBoardId(boardId);
	}
	
	public List<BoardDTO> readAllLikeTitle(String title) {
		return boardDao.readAllLikeTitle(title);
	}
	
	public void increaseLike(int boardId) {
		boardDao.increaseLike(boardId);
	}
	
	public void decreaseLike(int boardId) {
		boardDao.decreaseLike(boardId);
	}
	
	public void increaseDislike(int boardId) {
		boardDao.increaseDislike(boardId);
	}
	
	public void decreaseDislike(int boardId) {
		boardDao.decreaseDislike(boardId);
	}
	
	public void increaseViewed(int boardId) {
		boardDao.increaseViewed(boardId);
	}
}
