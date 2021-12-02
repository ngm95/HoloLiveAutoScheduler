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
		
		<div class="jumbotron" style="background-color: #f1f1f1">
			<h1>Holoduler</h1>
			<div class="jumbotron-board">
				
				<div id="indexNotice">
					<p style="margin-bottom: 0px">
						주기적으로 홀로라이브 JP/EN 멤버들의 유튜브 활동을 확인해 스케줄표를 구성하는 사이트입니다.<br /> 실시간 방송 상태와 다소 시간 차이가 발생할 수 있습니다.<br /> <br />
						<h2>메뉴 설명</h2>
						&nbsp;&nbsp;&nbsp;&nbsp;- <a style="font-size: 1.5em">스케줄 표</a> : 현재 라이브 중인 영상, 예약된 영상, 최근 1일 내에 종료된 영상 리스트를 보여줍니다.<br /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;테두리 색에 따라 영상의 상태를 쉽게 확인할 수 있습니다. (<b><a style="color: red">빨간색 - 라이브 중</a>, <a style="color: green">초록색 - 예약됨</a>, <a style="color: gray">회색 - 종료됨</a></b>)<br> &nbsp;&nbsp;&nbsp;&nbsp;- <a
							style="font-size: 1.5em">멀티 뷰</a> : 현재 라이브 중인 방송이나 1시간 내에 시작할 방송을 최대 9개까지 동시에 시청할 수 있습니다.<br /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;멀티 뷰 페이지에서 상단 메뉴 바에 있는 프로필 아이콘을 클릭하면 자동으로 영상이 추가됩니다.<br /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;방송 오른쪽 아래에 있는 버튼으로 시청중인 영상을 제거할 수 있습니다.<br />
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;영상에서 오류가 발생하면 영상 제거 후 다시 추가해주세요.<br /> &nbsp;&nbsp;&nbsp;&nbsp;- <a style="font-size: 1.5em">게시판</a> : 유튜브에서 아카이브 영상이나 클립/키리누키 영상을 공유할 수 있습니다.<br /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;홀로라이브 혹은 버츄얼 유튜버 관련 영상만 올려 주시길 바랍니다.<br />
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;적절하지 않은 영상은 삭제될 수 있습니다.<br />
					</p>
					<h1 style="color:pink">Thanks to and Powered By <a href="https://holodex.stoplight.io/">Holodex.net API</a></h1>
				</div>
			</div>
		</div>

		<%@ include file="/view/includes/09_footer.jsp"%>
	</div>
</body>

<script type="text/javascript">
	function sizing() {
		$('.jumbotron').height((window.innerHeight - 140));
		$('.jumbotron-board').height((window.innerHeight - 280));
		$('#indexNotice').attr('style','margin-top:' + (window.innerHeight - 280-370) / 2 + "px");
	}

	$(document).ready(function() {
		sizing();
	});

	$(window).resize(function() {
		sizing();
	});
</script>
</html>