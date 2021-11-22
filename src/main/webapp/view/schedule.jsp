<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
<%@ include file="/view/includes/00_head.jsp"%>

<title>메인 페이지</title>
</head>
<body class="main-pages contents-wrap">
	<div class="container" style="height: 100%">
		<%@ include file="/view/includes/03_header.jsp"%>
		<div class="jumbotron">
			<h3>빠른 이동</h3>
			<ul>
				<li><a href="#live">라이브 중인 영상</a></li>
				<li><a href="#upcoming">예약된 영상</a></li>
				<li><a href="#recent">최근 1일 영상</a></li>
			</ul>
			
			
		</div>
		<div class="jumbotron">
			<p id="live" style="margin-bottom:0px"><h3>라이브 중인 영상</h3></p>
			<div class="jumbotron-board">
				<c:choose>
					<c:when test="${empty liveList}">
						
					</c:when>
					<c:otherwise>
						
					</c:otherwise>
				</c:choose>
			</div>
			
			<p id="upcoming" style="margin-bottom:0px; margin-top:100px"><h3>예약된 영상</h3></p>
			<div class="jumbotron-board">
				<c:choose>
					<c:when test="${empty liveList}">
						
					</c:when>
					<c:otherwise>
						
					</c:otherwise>
				</c:choose>
			</div>
			
			<p id="recent" style="margin-bottom:0px; margin-top:100px"><h3>최근 1일 영상</h3></p>
			<div class="jumbotron-board">
				<div class="d-flex">
					<div class="row row-cols-auto">
						<c:forEach var="video" items="${videos}" varStatus="status">
							<div class="col" style="margin-bottom:15px; padding-left:8px; pdding-right:8px">
								<div class="d-flex">
									<c:choose>
										<c:when test="${empty video.actualStartTime}">
											<div class="card" style="cursor: pointer; border-width:thick; border-color:green" onclick="window.open('https://www.youtube.com/watch?v=${video.videoId}')">
												<div class="d-flex flex-column">
													<div class="d-flex justify-content-between">
														<div class="d-flex flex-row">
															<div class="d-flex">
																<img src="${video.profilePath}" style="border-radius: 50%;width: 40px;border: 2px white solid;">
															</div>
															<div class="d-flex" style="margin-left:7px; margin-top:8px">
																${video.memberName}
															</div>
														</div>
														<div class="d-flex" style="margin-top:8px">
															<b>${video.scheduledStartTime}</b>
														</div>
													</div>
													<div class="d-flex">
														<img src="${video.thumbnailPath}">
													</div>
												</div>
											</div>
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${empty video.actualEndTime}">
													<div class="card" style="cursor: pointer; border-width:thick; border-color:red" onclick="window.open('https://www.youtube.com/watch?v=${video.videoId}')">
														<div class="d-flex flex-column">
															<div class="d-flex justify-content-between">
																<div class="d-flex flex-row">
																	<div class="d-flex">
																		<img src="${video.profilePath}" style="border-radius: 50%; width: 40px; border: 2px white solid;">
																	</div>
																	<div class="d-flex" style="margin-left: 7px; margin-top: 8px">
																		${video.memberName}
																	</div>
																</div>
																<div class="d-flex" style="margin-top: 8px">
																	<b>${video.scheduledStartTime}</b>
																</div>
															</div>
															<div class="d-flex">
																<img src="${video.thumbnailPath}">
															</div>
														</div>
													</div>
												</c:when>
												<c:otherwise>
													<div class="card" style="cursor: pointer; border-width:thick; border-color:gray" onclick="window.open('https://www.youtube.com/watch?v=${video.videoId}')">
														<div class="d-flex flex-column">
															<div class="d-flex justify-content-between">
																<div class="d-flex flex-row">
																	<div class="d-flex">
																		<img src="${video.profilePath}" style="border-radius: 50%; width: 40px; border: 2px white solid;">
																	</div>
																	<div class="d-flex" style="margin-left: 7px; margin-top: 8px">
																		${video.memberName}
																	</div>
																</div>
																<div class="d-flex" style="margin-top: 8px">
																	<b>${video.scheduledStartTime}</b>
																</div>
															</div>
															<div class="d-flex">
																<img src="${video.thumbnailPath}">
															</div>
														</div>
													</div>
												</c:otherwise>
											</c:choose>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</c:forEach>
					</div>
				</div>
				
			</div>
		</div>
		
		<%@ include file="/view/includes/09_footer.jsp"%>
	</div>
</body>
</html>