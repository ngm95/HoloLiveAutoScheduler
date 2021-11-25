<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
<%@ include file="/view/includes/00_head.jsp"%>

<title>멀티뷰</title>
</head>
<body class="main-pages contents-wrap" style="padding:0px 0px 0px 0px; margin-left:12px; margin-right:0px; margin-top:80px">
	<div class="container-iframe" style="height: 100%; padding:0px 0px 0px 0px; margin:0px 0px 0px 0px">
		<%@ include file="/view/includes/03_header.jsp"%>
		
		<div class="jumbotron-iframe" style="padding:0px 0px 0px 0px; margin:0px 0px 0px 0px">
			<div id="iframeDiv" class="row row-cols-auto">
				
			</div>
		</div>
		
		<%@ include file="/view/includes/09_footer.jsp"%>
	</div>
</body>
</html>