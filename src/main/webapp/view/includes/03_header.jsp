<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<nav class="navbar navbar-light bg-light fixed-top" style="padding-top:5px; padding-bottom:5px;">
	<div class="container-fluid">
		<button class="navbar-toggler" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvasNavbar" aria-controls="offcanvasNavbar">
			<span class="navbar-toggler-icon"></span>
		</button>
		
		<div class="d-flex flex-row">
			<div class="d-flex">
				<button class="btn btn-warning" type="button" id="refresh" style="margin-right:5px">
					<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-arrow-clockwise" viewBox="0 0 16 16">
					  <path fill-rule="evenodd" d="M8 3a5 5 0 1 0 4.546 2.914.5.5 0 0 1 .908-.417A6 6 0 1 1 8 2v1z" />
					  <path d="M8 4.466V.534a.25.25 0 0 1 .41-.192l2.36 1.966c.12.1.12.284 0 .384L8.41 4.658A.25.25 0 0 1 8 4.466z" />
					</svg>
				</button>
			</div>
			<div id="videosList" class="d-flex"></div>
		</div>
		<a class="navbar-brand" href="/"><b>Hololive</b> Auto <b>Scheduler</b></a>

		<div class="offcanvas offcanvas-start" tabindex="-1" id="offcanvasNavbar" aria-labelledby="offcanvasNavbarLabel">
			<div class="offcanvas-header">
				<h5 class="offcanvas-title" id="offcanvasNavbarLabel">사이드 메뉴</h5>
				<button type="button" class="btn-close text-reset" data-bs-dismiss="offcanvas" aria-label="Close"></button>
			</div>
			<div class="offcanvas-body">
				<ul class="navbar-nav justify-content-end flex-grow-1 pe-3">
					<li class="nav-item dropdown"><a class="nav-link dropdown-toggle" href="/livestream/schedule" id="offcanvasNavbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false"> 스케쥴 </a>
						<ul class="dropdown-menu" aria-labelledby="offcanvasNavbarDropdown">
							<li><a class="dropdown-item" href="/livestream/schedule#live">라이브 중인 방송</a></li>
							<li><a class="dropdown-item" href="/livestream/schedule#upcoming">예약된 방송</a></li>
							<li><a class="dropdown-item" href="/livestream/schedule#completed">종료된 최근 방송</a></li>
						</ul>
					</li>
					<li class="nav-item"><a class="nav-link active" aria-current="page" href="/livestream/multiview">멀티뷰</a></li>
					<li class="nav-item"><a class="nav-link active" aria-current="page" href="/board/main">게시판</a></li>
				</ul>
			</div>
		</div>
	</div>
</nav>
