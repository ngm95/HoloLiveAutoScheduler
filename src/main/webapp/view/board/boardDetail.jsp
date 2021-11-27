<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/view/includes/00_head.jsp"%>
<meta id="boardId" name="boardId" content="${boardId}" />
<title>게시판</title>
</head>
<body class="board-pages">
	<div class="container contents-wrap" style="height: 100%; margin-top: 80px">
		<%@ include file="/view/includes/03_header.jsp"%>
		<jsp:include page="/view/includes/noticeModal.jsp"></jsp:include>

		<div class="jumbotron" style="background-color: #f1f1f1">
			<c:if test="${not empty boardDto}">

				<div class="d-flex justify-content-between">
					<div class="d-flex flex-row">
						<div class="d-flex align-items-center">
							<div class="d-flex">제목&nbsp;</div>
							<div class="d-flex" style="padding: 5px 10px 5px 10px; border-radius: 0.5rem; border: 1px gray solid; background-color: #e9ecef">${boardDto.title}</div>
						</div>
					</div>
					<div class="d-flex flex-row">
						<div class="d-flex align-items-center">
							<div class="d-flex">좋아요&nbsp;</div>
							<div id="likeValue" class="d-flex" style="padding: 5px 10px 5px 10px; border-radius: 0.5rem; border: 1px gray solid; background-color: #e9ecef">${boardDto.love}</div>
							<div class="d-flex" style="margin-left: 10px">
								<button id="thumbsUp" class="btn btn-outline-success">
									<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-hand-thumbs-up" viewBox="0 0 16 16"> <path
										d="M8.864.046C7.908-.193 7.02.53 6.956 1.466c-.072 1.051-.23 2.016-.428 2.59-.125.36-.479 1.013-1.04 1.639-.557.623-1.282 1.178-2.131 1.41C2.685 7.288 2 7.87 2 8.72v4.001c0 .845.682 1.464 1.448 1.545 1.07.114 1.564.415 2.068.723l.048.03c.272.165.578.348.97.484.397.136.861.217 1.466.217h3.5c.937 0 1.599-.477 1.934-1.064a1.86 1.86 0 0 0 .254-.912c0-.152-.023-.312-.077-.464.201-.263.38-.578.488-.901.11-.33.172-.762.004-1.149.069-.13.12-.269.159-.403.077-.27.113-.568.113-.857 0-.288-.036-.585-.113-.856a2.144 2.144 0 0 0-.138-.362 1.9 1.9 0 0 0 .234-1.734c-.206-.592-.682-1.1-1.2-1.272-.847-.282-1.803-.276-2.516-.211a9.84 9.84 0 0 0-.443.05 9.365 9.365 0 0 0-.062-4.509A1.38 1.38 0 0 0 9.125.111L8.864.046zM11.5 14.721H8c-.51 0-.863-.069-1.14-.164-.281-.097-.506-.228-.776-.393l-.04-.024c-.555-.339-1.198-.731-2.49-.868-.333-.036-.554-.29-.554-.55V8.72c0-.254.226-.543.62-.65 1.095-.3 1.977-.996 2.614-1.708.635-.71 1.064-1.475 1.238-1.978.243-.7.407-1.768.482-2.85.025-.362.36-.594.667-.518l.262.066c.16.04.258.143.288.255a8.34 8.34 0 0 1-.145 4.725.5.5 0 0 0 .595.644l.003-.001.014-.003.058-.014a8.908 8.908 0 0 1 1.036-.157c.663-.06 1.457-.054 2.11.164.175.058.45.3.57.65.107.308.087.67-.266 1.022l-.353.353.353.354c.043.043.105.141.154.315.048.167.075.37.075.581 0 .212-.027.414-.075.582-.05.174-.111.272-.154.315l-.353.353.353.354c.047.047.109.177.005.488a2.224 2.224 0 0 1-.505.805l-.353.353.353.354c.006.005.041.05.041.17a.866.866 0 0 1-.121.416c-.165.288-.503.56-1.066.56z" />
									</svg>
								</button>
							</div>
						</div>
					</div>
					<div class="d-flex flex-row">
						<div class="d-flex align-items-center">
							<div class="d-flex">싫어요&nbsp;</div>
							<div id="dislikeValue" class="d-flex" style="padding: 5px 10px 5px 10px; border-radius: 0.5rem; border: 1px gray solid; background-color: #e9ecef">${boardDto.dislike}</div>
							<div class="d-flex" style="margin-left: 10px">
								<button id="thumbsDown" class="btn btn-outline-danger">
									<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-hand-thumbs-down" viewBox="0 0 16 16"> <path
										d="M8.864 15.674c-.956.24-1.843-.484-1.908-1.42-.072-1.05-.23-2.015-.428-2.59-.125-.36-.479-1.012-1.04-1.638-.557-.624-1.282-1.179-2.131-1.41C2.685 8.432 2 7.85 2 7V3c0-.845.682-1.464 1.448-1.546 1.07-.113 1.564-.415 2.068-.723l.048-.029c.272-.166.578-.349.97-.484C6.931.08 7.395 0 8 0h3.5c.937 0 1.599.478 1.934 1.064.164.287.254.607.254.913 0 .152-.023.312-.077.464.201.262.38.577.488.9.11.33.172.762.004 1.15.069.13.12.268.159.403.077.27.113.567.113.856 0 .289-.036.586-.113.856-.035.12-.08.244-.138.363.394.571.418 1.2.234 1.733-.206.592-.682 1.1-1.2 1.272-.847.283-1.803.276-2.516.211a9.877 9.877 0 0 1-.443-.05 9.364 9.364 0 0 1-.062 4.51c-.138.508-.55.848-1.012.964l-.261.065zM11.5 1H8c-.51 0-.863.068-1.14.163-.281.097-.506.229-.776.393l-.04.025c-.555.338-1.198.73-2.49.868-.333.035-.554.29-.554.55V7c0 .255.226.543.62.65 1.095.3 1.977.997 2.614 1.709.635.71 1.064 1.475 1.238 1.977.243.7.407 1.768.482 2.85.025.362.36.595.667.518l.262-.065c.16-.04.258-.144.288-.255a8.34 8.34 0 0 0-.145-4.726.5.5 0 0 1 .595-.643h.003l.014.004.058.013a8.912 8.912 0 0 0 1.036.157c.663.06 1.457.054 2.11-.163.175-.059.45-.301.57-.651.107-.308.087-.67-.266-1.021L12.793 7l.353-.354c.043-.042.105-.14.154-.315.048-.167.075-.37.075-.581 0-.211-.027-.414-.075-.581-.05-.174-.111-.273-.154-.315l-.353-.354.353-.354c.047-.047.109-.176.005-.488a2.224 2.224 0 0 0-.505-.804l-.353-.354.353-.354c.006-.005.041-.05.041-.17a.866.866 0 0 0-.121-.415C12.4 1.272 12.063 1 11.5 1z" />
									</svg>
								</button>
							</div>
						</div>
					</div>
					<div class="d-flex flex-row">
						<div class="d-flex align-items-center">
							<div class="d-flex">조회수&nbsp;</div>
							<div class="d-flex" style="padding: 5px 10px 5px 10px; border-radius: 0.5rem; border: 1px gray solid; background-color: #e9ecef">${boardDto.viewed}</div>
						</div>
					</div>
					<div class="d-flex">
						<div class="d-flex align-items-center">
							<button class="btn btn-info" onclick="location.href='/board/main/${page}'">목록</button>
						</div>
					</div>
				</div>

			</c:if>
			<div class="jumbotron-board">
				<c:choose>
					<c:when test="${empty boardDto}">
						<div class="d-flex justify-content-center" id="noBoardNotice">
							<div class="d-flex align-items-center">
								<div class="d-flex">
									<svg xmlns="http://www.w3.org/2000/svg" width="100" height="100" fill="currentColor" class="bi bi-emoji-frown" viewBox="0 0 16 16"> <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z" /> <path
										d="M4.285 12.433a.5.5 0 0 0 .683-.183A3.498 3.498 0 0 1 8 10.5c1.295 0 2.426.703 3.032 1.75a.5.5 0 0 0 .866-.5A4.498 4.498 0 0 0 8 9.5a4.5 4.5 0 0 0-3.898 2.25.5.5 0 0 0 .183.683zM7 6.5C7 7.328 6.552 8 6 8s-1-.672-1-1.5S5.448 5 6 5s1 .672 1 1.5zm4 0c0 .828-.448 1.5-1 1.5s-1-.672-1-1.5S9.448 5 10 5s1 .672 1 1.5z" /> </svg>
								</div>
								<div class="d-flex">
									<a style="font-size: 2.5em">&nbsp;해당되는 게시글이 없거나 삭제되었습니다.</a>
								</div>
							</div>
						</div>
					</c:when>
					<c:otherwise>
						<div class="d-flex justify-content-center">
							<iframe id="youtubeVideo" width="560" height="315" src="https://www.youtube.com/embed/${boardDto.videoId}" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
						</div>
					</c:otherwise>
				</c:choose>
			</div>
		</div>

		<%@ include file="/view/includes/09_footer.jsp"%>
	</div>
</body>

<script type="text/javascript">
	
	
	function sizing() {
		if ($('#noBoardNotice').length) {
			$('.jumbotron').height((window.innerHeight - 140));
			$('.jumbotron-board').height((window.innerHeight - 200));
			$('#noBoardNotice').attr('style','margin-top:' + (window.innerHeight - 300) / 2 + "px");
		}

		if ($('#youtubeVideo').length) {
			var widthMax = window.innerWidth - 200;
			var heightMax = window.innerHeight - 250;

			$('#youtubeVideo').width(widthMax);
			$('#youtubeVideo').height(heightMax);
		}
	}

	$(document).ready(function() {
		sizing();
	});

	$(window).resize(function() {
		sizing();
	});
	
	var thumbsUp = false;
	var thumbsDown = false;
	
	var boardId = $("meta[name='boardId']").attr("content");
	
	$(document).on('click', '#thumbsUp', function() {
		if (thumbsUp == false) {
			$.ajax({
				url: "/board/increaseLike/" + boardId,
				method: "get",
				dataType: "json",
				contentType: "application/json;charset=UTF-8",
				success: function(result) {
					$('#likeValue').empty();
					$('#likeValue').append(result);
				},
				error: function() {
					
				}
			});
			thumbsUp = true;
			$('#thumbsUp').attr('style', 'background-color:green; color:white');
		}
		else {
			$.ajax({
				url: "/board/decreaseLike/" + boardId,
				method: "get",
				dataType: "json",
				contentType: "application/json;charset=UTF-8",
				success: function(result) {
					$('#likeValue').empty();
					$('#likeValue').append(result);
				},
				error: function() {
					
				}
			});
			thumbsUp = false;
			$('#thumbsUp').attr('style', '');
		}
	});
	
	$(document).on('click', '#thumbsDown', function() {
		if (thumbsDown == false) {
			$.ajax({
				url: "/board/increaseDislike/" + boardId,
				method: "get",
				dataType: "json",
				contentType: "application/json;charset=UTF-8",
				success: function(result) {
					$('#dislikeValue').empty();
					$('#dislikeValue').append(result);
				},
				error: function() {
					
				}
			});
			thumbsDown = true;
			$('#thumbsDown').attr('style', 'background-color:red; color:white');
		}
		else {
			$.ajax({
				url: "/board/decreaseDislike/" + boardId,
				method: "get",
				dataType: "json",
				contentType: "application/json;charset=UTF-8",
				success: function(result) {
					$('#dislikeValue').empty();
					$('#dislikeValue').append(result);
				},
				error: function() {
					
				}
			});
			thumbsDown = false;
			$('#thumbsDown').attr('style', '');
		}
	});
</script>
</html>