package com.hololive.livestream.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hololive.livestream.DAO.BoardDAO;
import com.hololive.livestream.DTO.BoardDTO;
import com.hololive.livestream.util.BoardMinMax;

@Service
public class BoardService {
	@Autowired
	BoardDAO boardDao;
	
	public void create(BoardDTO boardDto) {
		boardDao.create(boardDto);
	}
	
	public int readBoardId(BoardDTO boardDto) {
		return boardDao.readBoardId(boardDto);
	}
	
	public List<BoardDTO> readLimitList(BoardMinMax bmm) {
		return boardDao.readLimitList(bmm);
	}
	
	public int readMaxCount() {
		return boardDao.readMaxCount();
	}
	
	public void increaseLike(int boardId) {
		boardDao.increaseLike(boardId);
	}
	
	public void increaseDislike(int boardId) {
		boardDao.increaseDislike(boardId);
	}
	
	public void increaseViewed(int boardId) {
		boardDao.increaseViewed(boardId);
	}
}
