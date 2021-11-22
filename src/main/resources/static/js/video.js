$(document).ready(function() {
	var token = $("meta[name='_csrf_token']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajax({
		method : "get",
		dataType : "json",
		contentType:"application/json;charset=UTF-8",
		beforeSend : function(xhr) {
			xhr.setRequestHeader(header, token);
		},
		success : function(videos) {
			$('#contentBody').empty();
			
			for (var i in videos) {
				videos[i].memberName;
				videos[i].scheduledStartTime;
				videos[i].actualStartTime;
				var input = "";
				if (videos[i].actualEndTime != null) {
					input = "<a href=\"https://www.youtube.com/watch?v=" + video.videoId  + " style=\"border-style:dotted\">";
				} else if (videos[i].actualStartTime != null) {
					input = "<a href=\"https://www.youtube.com/watch?v=" + video.videoId  + "> style=\"color:red\"";
				} else {
					input = "<a href=\"https://www.youtube.com/watch?v=" + video.videoId  + "> style=\"color:blue\"";
				}
						input += "<div class=\"container\">";
							input += "<div class=\"d-flex flex-column\">";
								input += "<div class=\"d-flex flex-row justify-content-between\">";
									input += "<div class=\"d-flex flex-column\">" + video.scheduledStartTime;
									input += "</div>";
									input += "<div class=\"d-flex flex-column\">" + video.memberName;
									input += "</div>";
								input += "</div>";
							input += "<div class=\"d-flex flex-row\">";
								input += "<img src=\"" + video.thumbnailPath + ">";
							input += "</div>";
						input += "</div>";
					input += "</div>";
				input += "</a>"
				$('#contentBody').append(input);
			}
		},
		error : function() {
			$('#contentBody').append("<p>오류가 발생했습니다.</p>");
		}
	});
});