<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<div class="modal fade" id="postModal" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title">새로운 글 작성</h5>
				<button type="button" class="btn-close" data-bs-dismiss="modal"></button>
			</div>
			<div class="modal-body">
				<form:form modelAttribute="post" action="/board/newPost" method="post">
					<a style="color:red">※주소나 링크 중 하나만 넣어도 됩니다.</a>
					<table class="table">
						<tr>
							<td>제목</td>
							<td><form:input class="form-control" type="text" path="title" placeholder="제목" /></td>
						</tr>
						<tr>
							<td>영상 유튜브 주소</td>
							<td><form:input class="form-control" type="text" path="longPath" aria-describedby="basic-addon1" placeholder="유튜브 주소" /></td>
						</tr>
						<tr>
							<td>영상 공유 링크</td>
							<td><form:input class="form-control" type="text" path="shortPath" aria-describedby="basic-addon1" placeholder="공유 링크" /></td>
						</tr>
						<tr>
							<td>설명</td>
							<td><form:textarea class="form-control" type="text" rows="5" path="boardDetail" placeholder="영상에 대한 설명" /></td>
						</tr>
					</table>
					<button type="button" class="btn btn-secondary" data-bs-dismiss="modal" style="float: right; margin-left: 10px">닫기</button>
					<button id="modalSubmit" type="submit" class="btn btn-success" style="float: right">제출</button>
				</form:form>
			</div>
			<div class="modal-footer"></div>

		</div>
	</div>
</div>