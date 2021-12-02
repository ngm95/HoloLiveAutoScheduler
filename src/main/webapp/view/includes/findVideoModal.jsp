<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<div class="modal fade" id="findVideoModal" role="dialog">
	<div class="modal-dialog modal-fullscreen">
		<div class="modal-content">
			<div class="modal-header">
				<div class="d-flex flex-row">
					<div class="d-flex">
						<h5 class="modal-title" style="">새로운 방송 선택</h5>
					</div>
				</div>
				<div class="d-flex">
					<div class="btn-group" role="group" aria-label="Basic radio toggle button group">
						<input type="radio" class="btn-check" name="btnradio" id="liveRadio" autocomplete="off" checked> <label class="btn btn-outline-primary" for="liveRadio">현재 방송 중</label> <input type="radio" class="btn-check" name="btnradio" id="upcomingRadio" autocomplete="off"> <label class="btn btn-outline-primary" for="upcomingRadio">예정된 방송</label> <input type="radio" class="btn-check" name="btnradio" id="completedRadio" autocomplete="off"> <label class="btn btn-outline-primary"
							for="completedRadio">종료된 방송</label>
					</div>
				</div>

			</div>

			<div class="modal-body" id="liveBody">
				<div id="modalLiveList" class="row row-cols-auto"></div>
				<div id="modalUpcomingList" class="row row-cols-auto"></div>
				<div id="completedList">
					<div class="d-flex flex-column">
						<div name="calendarDiv" id="calendarDiv" class="d-flex justify-content-end" style="margin-bottom:15px">
							<button name="selectStart" class="btn btn-info" disabled>
								<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-calendar-range" viewBox="0 0 16 16">
							  <path d="M9 7a1 1 0 0 1 1-1h5v2h-5a1 1 0 0 1-1-1zM1 9h4a1 1 0 0 1 0 2H1V9z" />
							  <path d="M3.5 0a.5.5 0 0 1 .5.5V1h8V.5a.5.5 0 0 1 1 0V1h1a2 2 0 0 1 2 2v11a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V3a2 2 0 0 1 2-2h1V.5a.5.5 0 0 1 .5-.5zM1 4v10a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1V4H1z" />
							</svg>
							</button>
							<input type="text" id="start" value="" />
							<button name="selectEnd" class="btn btn-info" disabled>
								<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-calendar-range" viewBox="0 0 16 16">
							  <path d="M9 7a1 1 0 0 1 1-1h5v2h-5a1 1 0 0 1-1-1zM1 9h4a1 1 0 0 1 0 2H1V9z" />
							  <path d="M3.5 0a.5.5 0 0 1 .5.5V1h8V.5a.5.5 0 0 1 1 0V1h1a2 2 0 0 1 2 2v11a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V3a2 2 0 0 1 2-2h1V.5a.5.5 0 0 1 .5-.5zM1 4v10a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1V4H1z" />
							</svg>
							</button>
							<input type="text" id="end" value="" />
						</div>
					</div>
					<div class="d-flex">
						<div id="modalCompletedList" class="row row-cols-auto"></div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary" data-bs-dismiss="modal" style="float: right; margin-left: 10px">닫기</button>
			</div>
		</div>
	</div>
</div>