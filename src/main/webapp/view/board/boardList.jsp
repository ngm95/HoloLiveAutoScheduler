<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/view/includes/00_head.jsp"%>
<script src="/js/tooltip.js"></script>
<title>게시판</title>
</head>
<body class="board-pages">

	<div class="container contents-wrap" style="height: 100%; margin-top: 80px">
		<%@ include file="/view/includes/03_header.jsp"%>
		<jsp:include page="/view/includes/noticeModal.jsp"></jsp:include>

		<div class="jumbotron">
			<jsp:include page="/view/board/newPostModal.jsp"></jsp:include>
			
			<div class="d-flex justify-content-between">
				<div class="d-flex">
					<h3>
						<b>게시판</b>
					</h3>
				</div>
				<div class="d-flex">
					<button id="createBtn" type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#postModal" style="float: right">새로운 글 작성</button>
				</div>
			</div>
			
			<div class="jumbotron-board">

				<table class="table table-striped">
					<thead>
						<tr>
							<th scope="col">글 번호</th>
							<th scope="col">제목</th>
							<th scope="col">좋아요</th>
							<th scope="col">싫어요</th>
							<th scope="col">조회수</th>
							<th scope="col">시간</th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${empty boardList}">
								<tr>
									<th scope="row">#</th>
									<td></td>
									<td colspan="4">작성된 혹은 해당되는 글이 없습니다.</td>
								</tr>
								<c:forEach begin="0" end="13" step="1">
									<tr>
										<th scope="row">#</th>
										<td colspan="5"></td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<c:forEach var="board" items="${boardList}">
									<tr>
										<th scope="row">${board.boardId}</th>
										<td><a href="/board/boardDetail/${board.boardId}">${board.title}</a></td>
										<td>${board.love}</td>
										<td>${board.dislike}</td>
										<td>${board.viewed}</td>
										<td>${board.date}</td>
									</tr>
								</c:forEach>
								<c:forEach begin="${fn:length(boardList)+1}" end="15" step="1">
									<tr>
										<th scope="row">#</th>
										<td colspan="5"></td>
									</tr>
								</c:forEach>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
			</div>


			<nav>
			<ul class="pagination justify-content-center">
				<c:choose>
					<c:when test="${bmm.prev eq 'true'}">
						<li class="page-item"><a class="page-link" href="/lol/board/prev">이전</a></li>
					</c:when>
					<c:otherwise>
						<li class="page-item disabled"><a class="page-link">이전</a></li>
					</c:otherwise>
				</c:choose>

				<c:forEach begin="0" end="9" step="1" varStatus="status">
					<c:choose>
						<c:when test="${bmm.limit - (bmm.paging+status.index)*10  <= -10}">
							<li class="page-item disabled"><a class="page-link" href="/lol/board/${bmm.paging + status.index}">${bmm.paging + status.index}</a></li>
						</c:when>
						<c:otherwise>
							<li class="page-item"><a class="page-link" href="/lol/board/${bmm.paging + status.index}">${bmm.paging + status.index}</a></li>
						</c:otherwise>
					</c:choose>
				</c:forEach>

				<c:choose>
					<c:when test="${bmm.next eq 'true'}">
						<li class="page-item"><a class="page-link" href="/lol/board/next">다음</a></li>
					</c:when>
					<c:otherwise>
						<li class="page-item disabled"><a class="page-link">다음</a></li>
					</c:otherwise>
				</c:choose>
			</ul>
			</nav>
		</div>





		<!-- 		<div class="jumbotron"> -->
		<%-- 			<form:form modelAttribute="searchForm" action="/lol/findBoard" method="post"> --%>
		<!-- 				<div class="input-group"> -->
		<%-- 					<form:select class="form-select" path="checkRadio"> --%>
		<%-- 						<form:option value="groupName">제목</form:option> --%>
		<%-- 						<form:option value="groupOwner">작성자</form:option> --%>
		<%-- 						<form:option value="detail">세부 내용</form:option> --%>
		<%-- 					</form:select> --%>
		<%-- 					<form:input class="form-control" type="text" path="findDetail" placeholder="검색할 내용"/> --%>
		<%-- 					<form:button class="btn btn-outline-primary" type="submit">검색하기</form:button> --%>
		<!-- 				</div> -->
		<%-- 			</form:form> --%>
		<!-- 		</div> -->
		<%@ include file="/view/includes/09_footer.jsp"%>
	</div>

</body>
</html>