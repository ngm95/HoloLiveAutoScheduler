<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<%@ include file="/view/includes/00_head.jsp"%>

<title>스케줄</title>
</head>
<body class="main-pages contents-wrap" style="margin-top: 80px">
	<div class="container" style="height: 100%">
		<%@ include file="/view/includes/03_header.jsp"%>
		<jsp:include page="/view/includes/noticeModal.jsp"></jsp:include>
		
		<div class="jumbotron" style="background-color:#f1f1f1">
			<p id="live" style="margin-bottom: 0px">
			<h3>라이브 중인 방송</h3>
			</p>
			<div class="jumbotron-board">
				<c:choose>
					<c:when test="${not empty liveList}">
						<div class="row row-cols-auto">
							<c:forEach var="live" items="${liveList}" varStatus="status">
								<div class="col" style="margin-bottom: 15px; padding-left: 8px; pdding-right: 8px">
									<div class="d-flex">
										<div class="card" style="cursor: pointer; border-width: thick; border-color: red; width:330px; height:230px" onclick="window.open('https://www.youtube.com/watch?v=${live.videoId}')">
											<div class="d-flex flex-column">
												<div class="d-flex justify-content-between">
													<div class="d-flex flex-row">
														<div class="d-flex">
															<img src="${live.profilePath}" style="border-radius: 50%; width: 40px; border: 2px white solid;">
														</div>
														<div class="d-flex" style="margin-left: 7px; margin-top: 8px">${live.memberName}</div>
													</div>
													<div class="d-flex" style="margin-top: 8px">
														<b>${live.actualStartTime}</b>
													</div>
												</div>
												<div class="d-flex">
													<img src="${live.thumbnailPath}" style="width: 320px; height:180px;">
												</div>
											</div>
										</div>
									</div>
								</div>
							</c:forEach>
						</div>
					</c:when>
					<c:otherwise>

					</c:otherwise>
				</c:choose>
			</div>

			<p id="upcoming" style="margin-bottom: 0px; margin-top: 100px">
			<h3>예약된 방송</h3>
			</p>
			<div class="jumbotron-board">
				<c:choose>
					<c:when test="${not empty upcomingList}">
						<div class="row row-cols-auto">
							<c:forEach var="upcoming" items="${upcomingList}" varStatus="status">
								<div class="col" style="margin-bottom: 15px; padding-left: 8px; pdding-right: 8px">
									<div class="d-flex">
										<div class="card" style="cursor: pointer; border-width: thick; border-color: green; width:330px; height:230px" onclick="window.open('https://www.youtube.com/watch?v=${upcoming.videoId}')">
											<div class="d-flex flex-column">
												<div class="d-flex justify-content-between">
													<div class="d-flex flex-row">
														<div class="d-flex">
															<img src="${upcoming.profilePath}" style="border-radius: 50%; width: 40px; border: 2px white solid;">
														</div>
														<div class="d-flex" style="margin-left: 7px; margin-top: 8px">${upcoming.memberName}</div>
													</div>
													<div class="d-flex" style="margin-top: 8px">
														<b>${upcoming.scheduledStartTime}</b>
													</div>
												</div>
												<div class="d-flex">
													<img src="${upcoming.thumbnailPath}" style="width: 320px; height:180px;">
												</div>
											</div>
										</div>
									</div>
								</div>
							</c:forEach>
						</div>
					</c:when>
					<c:otherwise>

					</c:otherwise>
				</c:choose>
			</div>

			<p id="completed" style="margin-bottom: 0px; margin-top: 100px">
			<h3>종료된 최근 방송</h3>
			</p>
			<div class="jumbotron-board">
				<c:choose>
					<c:when test="${not empty completedList}">
						<div class="row row-cols-auto">
							<c:forEach var="completed" items="${completedList}" varStatus="status">
								<div class="col" style="margin-bottom: 15px; padding-left: 8px; pdding-right: 8px">
									<div class="d-flex">
										<div class="card" style="cursor: pointer; border-width: thick; border-color: gray; width:330px; height:230px" onclick="window.open('https://www.youtube.com/watch?v=${completed.videoId}')">
											<div class="d-flex flex-column">
												<div class="d-flex justify-content-between">
													<div class="d-flex flex-row">
														<div class="d-flex">
															<img src="${completed.profilePath}" style="border-radius: 50%; width: 40px; border: 2px white solid;">
														</div>
														<div class="d-flex" style="margin-left: 7px; margin-top: 8px">${completed.memberName}</div>
													</div>
													<div class="d-flex" style="margin-top: 8px">
														<b>${completed.actualStartTime}</b>
													</div>
												</div>
												<div class="d-flex">
													<img src="${completed.thumbnailPath}" style="width: 320px; height:180px;">
												</div>
											</div>
										</div>
									</div>
								</div>
							</c:forEach>
						</div>
					</c:when>
					<c:otherwise>

					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
</body>
</html>