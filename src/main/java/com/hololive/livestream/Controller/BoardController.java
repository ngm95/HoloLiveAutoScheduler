package com.hololive.livestream.Controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hololive.livestream.DTO.BoardDTO;
import com.hololive.livestream.Service.BoardService;
import com.hololive.livestream.util.BoardMinMax;

@Controller
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	private BoardService boardServ;
	
	private BoardMinMax bmm;
	private List<BoardDTO> boardList;
	
	/**
	 * 메인 게시판 페이지
	 * - 글을 최근 순서로 10개 단위 10페이지를 게시하고 이전/다음 버튼으로 다른 범위에 있는 글을 가져올 수 있음.
	 *   검색 버튼을 누르면 입력 폼에 입력된 정보와 체크박스에서 선택된 정보를 가지고 비슷한 글을 찾아서 그 글들을 보여줌.
	 */
	@RequestMapping("/main")
	public String lolBoard(RedirectAttributes rdAttributes, Model model) {
		bmm = new BoardMinMax(boardServ.readMaxCount());		// 새로운 bmm 객체를 만들어 둠
		boardList = boardServ.readLimitList(bmm);				// 최신 글 리스트(100개)를 불러옴 
		
		if (model.getAttribute("notice") != null) {										
			rdAttributes.addFlashAttribute("notice", model.getAttribute("notice"));
		}
		
		return "redirect:/board/main/" + bmm.getPaging();	// 첫 번째 페이지로 리다이렉트
	}
	
	/**
	 * 게시판 페이지에서 다음 버튼을 눌렀을 때 이벤트 처리
	 * - bmm을 이용해 새로 가져올 글의 범위와 prev, next 버튼의 활성화 여부를 계산해서 세션에 넣는다.
	 *   이후 게시판 페이지로 리다이렉트한다.
	 */
	@RequestMapping("/main/next")
	public String lolNextBoard() {
		bmm.next();											// bmm을 다음으로 넘김
		
		return "redirect:/board/main/" + bmm.getPaging();	// 첫 번째 페이지로 리다이렉트
	}

	/**
	 * 게시판 페이지에서 이전 버튼을 눌렀을 때 이벤트 처리
	 * - bmm을 이용해 새로 가져올 글의 범위와 prev, next 버튼의 활성화 여부를 계산해서 세션에 넣는다.
	 *   이후 게시판 페이지로 리다이렉트한다.
	 */
	@RequestMapping("/main/prev")
	public String lolPrevBoard() {
		bmm.prev();											// bmm을 이전으로 넘김
		
		return "redirect:/board/main/" + bmm.getPaging();	// 첫 번째 페이지로 리다이렉트
	}
	
	@RequestMapping("/main/{page}")
	public String lolBoardPaging(@PathVariable("page") int page, RedirectAttributes rdAttributes, Model model) {
		if (bmm.getLimit() - page*10 < -10)	{											// URL로 비활성화된 페이지로 접근하면 다시 첫 페이지로 이동하도록 함
			rdAttributes.addFlashAttribute("notice", new RuntimeException("존재하지 않는 게시글 페이지입니다."));
			return "redirect:/lol/board/" + bmm.getPaging();	
		}

		int startIdx = bmm.getMin()+(page-1)*10;										// 현재 페이지에서 볼 수 있는 글의 시작, 끝 인덱스 계산
		int endIdx = Math.min(bmm.getLimit(), startIdx+10);
		
		model.addAttribute("lgroupList", boardList.subList(startIdx, endIdx));			// 현재 페이지에서 볼 수 있는 글만 담아서 모델에 담음
		model.addAttribute("bmm", bmm);
		
		return "/board/boardList";
	}

	/**
	 * 게시판 페이지에서 상세정보로 찾기 버튼을 눌렀을 때 이벤트 처리
	 * 이후 게시판 페이지로 리다이렉트한다.
	 */
	@PostMapping("/findBoard")
	public String lolFindBoard(Model model) {
		// 어떤 옵션을 선택했는지 체크해서 적절한 Service, mapper를 통해 DB 접근을 통해 검색된 글 리스트를 받아 옴

		bmm = new BoardMinMax(boardList.size());							// 받아 온 글 리스트를 가지고 새로운 bmm객체를 생성

		return "redirect:/lol/board/" + bmm.getPaging();					// 첫 번째 페이지로 리다이렉트
	}

	@PostMapping(value="/newPost")
	public String newPost(@Valid @ModelAttribute("post") BoardDTO boardDto, BindingResult br, HttpServletRequest request, RedirectAttributes rdAttributes) {
		if (boardDto.getTitle().equals("")) {
			rdAttributes.addFlashAttribute("notice", new RuntimeException("게시글 제목은 필수로 입력해야 합니다."));
			return "redirect:/lol/board";
		}

		DateFormat format = new SimpleDateFormat("yy.MM.dd kk:mm:ss");		// '연.월.일 시간:분:초' 형식으로 시간을 구함
		String dateStr = format.format(Calendar.getInstance().getTime());
		boardDto.setDate(dateStr);
		
		try {
			boardServ.create(boardDto); 											// DB접근을 통해 게시글 생성 
			int boardId = boardServ.readBoardId(boardDto);						// 생성한 게시글의 고유 번호를 받아 옴
			
			return "redirect:/lol/boardDetail/" + boardId;						// 생성한 게시글로 리다이렉트
		} catch(Exception e) {
			rdAttributes.addFlashAttribute("notice", e);
			return "redirect:" + request.getHeader("Referer");
		}
	}
	
	/**
	 * 그룹의 세부 정보를 보여주는 페이지
	 * - 그룹 생성자와 현재까지의 멤버 목록을 보여줌
	 * - 그룹 멤버를 클릭하면 그 멤버의 세부 전적/티어를 볼 수 있는 페이지로 이동
	 * - 페이지 하단에는 메인 게시판으로 되돌아가는 버튼과 신청 페이지로 이동할 수 있는 버튼이 존재
	 */
	@RequestMapping("/boardDetail/{boardId}")
	public String lolBoardDetail(@PathVariable(value="boardId") int boardId, HttpServletRequest request, RedirectAttributes rdAttributes, Model model) {
		return "/board/boardDetail";
	}
}
