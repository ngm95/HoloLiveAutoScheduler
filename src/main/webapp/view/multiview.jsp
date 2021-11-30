<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
<%@ include file="/view/includes/00_head.jsp"%>
<script src="/js/findVideo.js"></script>
<title>멀티뷰</title>
</head>
<body class="main-pages contents-wrap" style="padding:0px 0px 0px 0px; margin-left:0px; margin-right:0px; margin-top:70px; background-color:#f1f1f1">
	<div class="container-iframe" style="height: 100%; padding:0px 0px 0px 0px; margin:0px 0px 0px 0px; background-color:#f1f1f1">
		<%@ include file="/view/includes/03_header.jsp"%>
		<jsp:include page="/view/includes/noticeModal.jsp"></jsp:include>
		<jsp:include page="/view/includes/findVideoModal.jsp"></jsp:include>
		
		<div class="jumbotron-iframe" style="padding:0px 0px 0px 0px; margin:0px 0px 0px 0px; background-color:#f1f1f1">
			<div id="iframeDiv" class="iframe-container" style="padding:0px 0px 0px 0px; margin:0px 0px 0px 0px; width:100; height:100%; background-color:#f1f1f1">
				
			</div>
		</div>
	</div>
</body>
</html>