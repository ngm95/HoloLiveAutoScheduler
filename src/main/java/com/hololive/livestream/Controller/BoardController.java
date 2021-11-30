package com.hololive.livestream.Controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.hololive.livestream.DAO.VideoDAO;
import com.hololive.livestream.DTO.APIDTO;
import com.hololive.livestream.DTO.BoardDTO;
import com.hololive.livestream.Service.BoardService;
import com.hololive.livestream.util.BoardMinMax;
import com.hololive.livestream.util.Post;

@Controller
@RequestMapping("/board")
public class BoardController {

	@ModelAttribute("post")
	public Post post() {
		return new Post();
	}

	@Autowired
	private BoardService boardServ;

	private BoardMinMax bmm; // 게시판의 페이징을 담당하는 객체
	private List<BoardDTO> boardList; // 게시판에 게시할 글 리스트

	@RequestMapping("/")
	public String toBoard1() {
		return "redirect:/board/main";
	}

	@RequestMapping("")
	public String toBoard2() {
		return "redirect:/board/main";
	}

	/**
	 * 메인 게시판 페이지 - 글을 최근 순서로 15개 단위 10페이지를 게시하고 이전/다음 버튼으로 다른 범위에 있는 글을 가져올 수 있음.
	 * 검색 버튼을 누르면 입력 폼에 입력된 정보와 체크박스에서 선택된 정보를 가지고 비슷한 글을 찾아서 그 글들을 보여줌.
	 */
	@RequestMapping("/main")
	public String board(RedirectAttributes rdAttributes, Model model) {
		bmm = new BoardMinMax(boardServ.readMaxCount()); // 새로운 bmm 객체를 만들어 둠
		boardList = boardServ.readLimitList(bmm);
		
		if (model.getAttribute("notice") != null) {
			rdAttributes.addFlashAttribute("notice", model.getAttribute("notice"));
		}

		return "redirect:/board/main/" + bmm.getPaging(); // 첫 번째 페이지로 리다이렉트
	}

	/**
	 * 게시판 페이지에서 다음 버튼을 눌렀을 때 이벤트 처리 - bmm을 이용해 새로 가져올 글의 범위와 prev, next 버튼의 활성화 여부를
	 * 계산해서 세션에 넣는다. 이후 게시판 페이지로 리다이렉트한다.
	 */
	@RequestMapping("/main/next")
	public String nextBoard() {
		bmm.next(); // bmm을 다음으로 넘김

		return "redirect:/board/main/" + bmm.getPaging(); // 첫 번째 페이지로 리다이렉트
	}

	/**
	 * 게시판 페이지에서 이전 버튼을 눌렀을 때 이벤트 처리 - bmm을 이용해 새로 가져올 글의 범위와 prev, next 버튼의 활성화 여부를
	 * 계산해서 세션에 넣는다. 이후 게시판 페이지로 리다이렉트한다.
	 */
	@RequestMapping("/main/prev")
	public String prevBoard() {
		bmm.prev(); // bmm을 이전으로 넘김

		return "redirect:/board/main/" + bmm.getPaging(); // 첫 번째 페이지로 리다이렉트
	}

	@RequestMapping("/main/{page}")
	public String boardPaging(@PathVariable("page") int page, RedirectAttributes rdAttributes, Model model) {
		if (bmm == null) {
			bmm = new BoardMinMax(boardServ.readMaxCount()); // 새로운 bmm 객체를 만들어 둠
		}
		if (boardList == null) {
			boardList = boardServ.readLimitList(bmm); // 최신 글 리스트(100개)를 불러옴
		}

		if (bmm.getLimit() - page * 10 < -10) { // URL로 비활성화된 페이지로 접근하면 다시 첫 페이지로 이동하도록 함
			rdAttributes.addFlashAttribute("notice", new RuntimeException("존재하지 않는 게시글 페이지입니다."));
			return "redirect:/board/main/" + bmm.getPaging();
		}

		int startIdx = bmm.getMin() + (page - 1) * 10; // 현재 페이지에서 볼 수 있는 글의 시작, 끝 인덱스 계산
		int endIdx = Math.min(bmm.getLimit(), startIdx + 10);
		
		bmm.setNow(page);

		model.addAttribute("boardList", boardList.subList(startIdx, endIdx)); // 현재 페이지에서 볼 수 있는 글만 담아서 모델에 담음
		model.addAttribute("bmm", bmm);

		return "/board/boardList";
	}

	/**
	 * 게시판 페이지에서 상세정보로 찾기 버튼을 눌렀을 때 이벤트 처리 이후 게시판 페이지로 리다이렉트한다.
	 */
	@PostMapping("/findBoard")
	public String findBoard(@RequestParam("title") String title, Model model) {
		boardList = boardServ.readAllLikeTitle(title);		// 비슷한 제목을 가진 게시물을 검색해서 받아옴
		bmm = new BoardMinMax(boardList.size()); 			// 받아 온 글 리스트를 가지고 새로운 bmm객체를 생성

		return "redirect:/board/main/" + bmm.getPaging(); // 첫 번째 페이지로 리다이렉트
	}

	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private static YouTube youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
		public void initialize(HttpRequest request) throws IOException {
		}
	}).setApplicationName("HoloLiveAutoScheduler").build();

	@Autowired
	private VideoDAO videoDao;
	
	public boolean checkValidVideo(String videoId) {
		try {
			YouTube.Videos.List videos = youtube.videos().list("id,snippet");
			videos.setId(videoId);
			videos.setMaxResults(1L);
			
			APIDTO api = videoDao.readMinQuotasAPIKey();
			if (api.getQuota() > 9999)
				throw new RuntimeException("\t\t하루 할당량이 초과되었습니다.");
			videos.setKey(api.getApiKey());
			videoDao.increaseQuotas1(api.getApiKey());
			
			List<Video> videoList = videos.execute().getItems();

			if (videoList.isEmpty()) 
				return false;
			else 
				return true;
		} catch (IOException ioe) {
			return false;
		} catch (RuntimeException re) {
			return false;
		}
	}

	/**
	 * 게시글 생성 모달창으로부터 입력받은 정보를 가지고 게시글을 만드는 메소드
	 * 
	 * 게시글이 입력되지 않거나 유튜브 영상URL과 공유URL이 모두 입력되지 않는다면 입력 정보가 부족하다는 알림을 보내고 게시글 첫 페이지로
	 * 리다이렉트한다.
	 */
	@PostMapping("/newPost")
	public String newPost(@Valid @ModelAttribute("post") Post post, BindingResult br, Model model, HttpServletRequest request, RedirectAttributes rdAttributes) {
		BoardDTO boardDto = new BoardDTO();

		if (post.getTitle().equals("")) {
			rdAttributes.addFlashAttribute("notice", new RuntimeException("게시글 제목은 필수로 입력해야 합니다."));
			return "redirect:/board/main";
		} else {
			boardDto.setTitle(post.getTitle());
		}
		
		try {
			if (post.getLongPath().equals("")) {
				if (post.getShortPath().equals(""))
					throw new RuntimeException("영상 주소는 필수로 입력해야 합니다.");
				else {
					boardDto.setVideoIdWithShortPath(post.getShortPath());
					if (!checkValidVideo(boardDto.getVideoId()))
						throw new RuntimeException("정상적이지 않은 주소입니다.");
				}
			} 
			else {
				boardDto.setVideoIdWithLongPath(post.getLongPath());
				if (!checkValidVideo(boardDto.getVideoId()))
					throw new RuntimeException("정상적이지 않은 주소입니다.");
			}
		} catch (RuntimeException re) {
			rdAttributes.addFlashAttribute("notice", re);
			return "redirect:/board/main";
		}
		
		boardDto.setVideoDetail(post.getBoardDetail());

		DateFormat format = new SimpleDateFormat("yy.MM.dd kk:mm:ss"); // '연.월.일 시간:분:초' 형식으로 시간을 구함
		String dateStr = format.format(Calendar.getInstance().getTime());
		boardDto.setDate(dateStr);

		try {
			boardServ.create(boardDto); // DB접근을 통해 게시글 생성

			int boardId = boardServ.readBoardId(boardDto); // 생성한 게시글의 고유 번호를 받아 옴
			return "redirect:/board/boardDetail/" + bmm.getNow() + "/" + boardId; // 생성한 게시글로 리다이렉트
		} catch (Exception e) {
			rdAttributes.addFlashAttribute("notice", e);
			return "redirect:" + request.getHeader("Referer");
		}
		
	}

	/**
	 * 그룹의 세부 정보를 보여주는 페이지
	 */
	@RequestMapping("/boardDetail/{page}/{boardId}")
	public String boardDetail(@PathVariable(value = "page") int page, @PathVariable(value = "boardId") int boardId, HttpServletRequest request, RedirectAttributes rdAttributes, Model model) {

		boardServ.increaseViewed(boardId);
		
		BoardDTO boardDto = boardServ.readByBoardId(boardId);
		model.addAttribute("boardDto", boardDto);
		model.addAttribute("boardId", boardId);
		
		model.addAttribute("page", page);
		
		return "/board/boardDetail";
	}
	
	@ResponseBody
	@RequestMapping("/increaseLike/{boardId}")
	public String increaseLike(@PathVariable("boardId") int boardId) {
		boardServ.increaseLike(boardId);
		return Integer.toString(boardServ.readByBoardId(boardId).getLove());
	}
	
	@ResponseBody
	@RequestMapping("/decreaseLike/{boardId}")
	public String decreaseLike(@PathVariable("boardId") int boardId) {
		boardServ.decreaseLike(boardId);
		return Integer.toString(boardServ.readByBoardId(boardId).getLove());
	}
	
	@ResponseBody
	@RequestMapping("/increaseDislike/{boardId}")
	public String increaseDislike(@PathVariable("boardId") int boardId) {
		boardServ.increaseDislike(boardId);
		return Integer.toString(boardServ.readByBoardId(boardId).getDislike());
	}
	
	@ResponseBody
	@RequestMapping("/decreaseDislike/{boardId}")
	public String decreaseDislike(@PathVariable("boardId") int boardId) {
		boardServ.decreaseDislike(boardId);
		return Integer.toString(boardServ.readByBoardId(boardId).getDislike());
	}
}
