<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<div class="modal fade" id="findVideoModal" role="dialog">
	<div class="modal-dialog modal-xl">
		<div class="modal-content">
			<div class="modal-header">
				<div class="d-flex flex-row">
					<div class="d-flex">
						<h5 class="modal-title" style="">새로운 방송 선택</h5>
					</div>
				</div>
				<div class="d-flex" >
					<div class="btn-group" role="group" aria-label="Basic radio toggle button group">
						<input type="radio" class="btn-check" name="btnradio" id="liveRadio" autocomplete="off" checked> <label class="btn btn-outline-primary" for="liveRadio">현재 방송 중</label> 
						<input type="radio" class="btn-check" name="btnradio" id="upcomingRadio" autocomplete="off"> <label class="btn btn-outline-primary" for="upcomingRadio">예정된 방송</label> 
						<input type="radio" class="btn-check" name="btnradio" id="completedRadio" autocomplete="off"> <label class="btn btn-outline-primary" for="completedRadio">종료된 방송</label>
					</div>
				</div>
				<div class="d-flex" style="float: right">
					<div class="btn btn-info">
						<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-calendar-range" viewBox="0 0 16 16">
						  <path d="M9 7a1 1 0 0 1 1-1h5v2h-5a1 1 0 0 1-1-1zM1 9h4a1 1 0 0 1 0 2H1V9z"/>
						  <path d="M3.5 0a.5.5 0 0 1 .5.5V1h8V.5a.5.5 0 0 1 1 0V1h1a2 2 0 0 1 2 2v11a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V3a2 2 0 0 1 2-2h1V.5a.5.5 0 0 1 .5-.5zM1 4v10a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1V4H1z"/>
						</svg>
					</div>
				</div>
			</div>
			
			<div class="modal-body" id="liveBody">
				<div class="row row-cols-auto">
					<c:forEach var="live" items="${liveList}" varStatus="status">
						<div id="${live.videoId}" name="select" class="col" data-bs-dismiss="modal" aria-label="Close" style="margin-bottom: 15px; padding-left: 8px; pdding-right: 8px">
							<div class="d-flex">
								<div class="card" style="border-width: thick; border-color: red; width: 165px; height: 140px">
									<div class="d-flex flex-column">
										<div class="d-flex justify-content-between">
											<div class="d-flex flex-row">
												<div class="d-flex">
													<img src="${live.profilePath}" style="border-radius: 50%; width: 40px; border: 1px white solid;">
												</div>
											</div>
											<div class="d-flex" style="margin-top: 8px">
												<b>${live.actualStartTime}</b>
											</div>
										</div>
										<div class="d-flex">
											<img src="${live.thumbnailPath}" style="width: 155px; height: 90px;">
										</div>
									</div>
								</div>
							</div>
						</div>
					</c:forEach>
				</div>
			</div>
			<div class="modal-body" id="upcomingBody">
				<div class="row row-cols-auto">
					<c:forEach var="upcoming" items="${upcomingList}" varStatus="status">
						<div id="${upcoming.videoId}" name="select" class="col" data-bs-dismiss="modal" aria-label="Close" style="margin-bottom: 15px; padding-left: 8px; pdding-right: 8px">
							<div class="d-flex">
								<div class="card" style="border-width: thick; border-color: green; width: 165px; height: 140px">
									<div class="d-flex flex-column">
										<div class="d-flex justify-content-between">
											<div class="d-flex flex-row">
												<div class="d-flex">
													<img src="${upcoming.profilePath}" style="border-radius: 50%; width: 40px; border: 1px white solid;">
												</div>
											</div>
											<div class="d-flex" style="margin-top: 8px">
												<b>${upcoming.scheduledStartTime}</b>
											</div>
										</div>
										<div class="d-flex">
											<img src="${upcoming.thumbnailPath}" style="width: 155px; height: 90px;">
										</div>
									</div>
								</div>
							</div>
						</div>
					</c:forEach>
				</div>
			</div>
			<div class="modal-body" id="completedBody">
				<div id="selectBtn" class="row row-cols-auto">
					<c:forEach var="completed" items="${completedList}" varStatus="status">
						<div id="${completed.videoId}" name="select" class="col" data-bs-dismiss="modal" aria-label="Close" style="margin-bottom: 15px; padding-left: 8px; pdding-right: 8px">
							<div class="d-flex">
								<div class="card" style="border-width: thick; border-color: gray; width: 165px; height: 140px">
									<div class="d-flex flex-column">
										<div class="d-flex justify-content-between">
											<div class="d-flex flex-row">
												<div class="d-flex">
													<img src="${completed.profilePath}" style="border-radius: 50%; width: 40px; border: 1px white solid;">
												</div>
											</div>
											<div class="d-flex" style="margin-top: 8px">
												<b>${completed.actualStartTime}</b>
											</div>
										</div>
										<div class="d-flex">
											<img src="${completed.thumbnailPath}" style="width: 155px; height: 90px;">
										</div>
									</div>
								</div>
							</div>
						</div>
					</c:forEach>
				</div>
			</div>
			<div class="modal-footer"></div>

		</div>
	</div>
</div>