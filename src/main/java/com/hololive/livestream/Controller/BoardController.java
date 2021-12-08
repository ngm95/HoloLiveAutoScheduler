package com.hololive.livestream.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.hololive.livestream.Service.BoardService;

@Controller
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private BoardService boardServ;

	@ResponseBody
	@RequestMapping("/increaseLike/{boardId}")
	public String increaseLike(@PathVariable("boardId") int boardId) {
		boardServ.increaseLike(boardId);
		return new Gson().toJson(boardServ.readByBoardId(boardId));
	}
	
	@ResponseBody
	@RequestMapping("/decreaseLike/{boardId}")
	public String decreaseLike(@PathVariable("boardId") int boardId) {
		boardServ.decreaseLike(boardId);
		return new Gson().toJson(boardServ.readByBoardId(boardId));
	}
	
	@ResponseBody
	@RequestMapping("/increaseDislike/{boardId}")
	public String increaseDislike(@PathVariable("boardId") int boardId) {
		boardServ.increaseDislike(boardId);
		return new Gson().toJson(boardServ.readByBoardId(boardId));
	}
	
	@ResponseBody
	@RequestMapping("/decreaseDislike/{boardId}")
	public String decreaseDislike(@PathVariable("boardId") int boardId) {
		boardServ.decreaseDislike(boardId);
		return new Gson().toJson(boardServ.readByBoardId(boardId));
	}
	
	@ResponseBody
	@RequestMapping("/getAllBoardList")
	public String getAllBoardList() {
		Gson gson = new Gson();
		return gson.toJson(boardServ.readAllBoard());
	}
	
	@ResponseBody
	@RequestMapping("/getAllBoardLikeTitle/{title}")
	public String getAllBoardLikeTitle(@PathVariable("title") String title) {
		Gson gson = new Gson();
		return gson.toJson(boardServ.readAllLikeTitle(title));
	}
}
