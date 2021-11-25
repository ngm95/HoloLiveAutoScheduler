$(document).ready(function() {
	$('#iframeDiv').height((window.innerHeight - 80));
	$('#iframeDiv').width((window.innerWidth));

	$(window).resize(function() {
		$('#iframeDiv').height((window.innerHeight - 80));
		$('#iframeDiv').width((window.innerWidth))

		var width = $('#iframeDiv').width();
		var height = $('#iframeDiv').height();
		var longest = width == Math.max(width, height) ? "width" : "height";
		if (iframes == 2) {
			height = height - 22;
		}
		else if (iframes == 3) {
			if (longest == "width") {
				width = width / 2;
				height = height - 22;
			}
			else
				height = height / 2 - 22;
		}
		else if (iframes <= 5) {
			width = width / 2;
			height = height / 2 - 22;
		} else if (iframes <= 7) {
			if (longest == "width") {
				width = width / 3;
				height = height / 2 - 22;
			}
			else {
				width = width / 2;
				height = height / 3 - 22;
			}
		} else if (iframes <= 10) {
			width = width / 3;
			height = height / 3 - 22;
		} else {
			return;
		}
		resize(width, height);
	});


	var token = $("meta[name='_csrf_token']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajax({
		url: "/livestream/videoInfo",
		method: "get",
		dataType: "json",
		contentType: "application/json;charset=UTF-8",
		beforeSend: function(xhr) {
			xhr.setRequestHeader(header, token);
		},
		success: function(videos) {
			for (var i in videos) {
				var html = '<div class=\'d-flex flex-column\'><div class=\'d-flex flex-row\'><img src=\'' + videos[i].thumbnailPath + '\' width=\'100%\' height=\'100%\'></div><div class=\'d-flex flex-row\'>예정 시각 : ' + videos[i].scheduledStartTime + '</div></div>';
				if (videos[i].actualStartTime != null) {
					$('#videosList').append("<div name=\"navProfile\" id=\"" + videos[i].videoId + "\" class=\"d-flex flex-column\"><button name=\"tooltip\" id=\"" + videos[i].videoId + "\" type=\"button\" class=\"btn btn-style\" style=\"padding:0px 0px 0px 0px\" data-bs-toggle=\"tooltip\" data-bs-html=\"true\" title=\"" + html + "\"><img src=\"" + videos[i].profilePath + "\" style=\"border-radius:50%; width:65px; border:2px red solid\"></button></div>");
				} else {
					$('#videosList').append("<div name=\"navProfile\" id=\"" + videos[i].videoId + "\" class=\"d-flex flex-column\"><button name=\"tooltip\" id=\"" + videos[i].videoId + "\" type=\"button\" class=\"btn btn-style\" style=\"padding:0px 0px 0px 0px\" data-bs-toggle=\"tooltip\" data-bs-html=\"true\" title=\"" + html + "\"><img src=\"" + videos[i].profilePath + "\" style=\"border-radius:50%; width:65px; border:2px green solid\"></button></div>");
				}
			}
			$('[name="tooltip"]').each(function() {
				var tooltip = new bootstrap.Tooltip($(this), {
					boundary: document.body // or document.querySelector('#boundary')
				});
			});
		},
		error: function() {
			$('#videosList').append("<p>오류가 발생했습니다.</p>");
		}
	});

	function refresh() {
		$('.tooltip').each(function() {
			$(this).remove();
		});

		$('#videosList').empty();
		$('#refresh').append('<a>Refresh...</a>');
		$.ajax({
			url: "/livestream/videoInfo",
			method: "get",
			dataType: "json",
			contentType: "application/json;charset=UTF-8",
			beforeSend: function(xhr) {
				xhr.setRequestHeader(header, token);
			},
			success: function(videos) {
				for (var i in videos) {
					var videoId = videos[i].videoId;
					var unique = true;
					$('#iframeDiv').find('iframe').each(function() {
						if ($(this).attr('id') == videoId) {
							unique = false;
						}
					});

					if (unique == true) {
						var html = '<div class=\'d-flex flex-column\'><div class=\'d-flex flex-row\'><img src=\'' + videos[i].thumbnailPath + '\' width=\'100%\' height=\'100%\'></div><div class=\'d-flex flex-row\'>예정 시각 : ' + videos[i].scheduledStartTime + '</div></div>';
						if (videos[i].actualStartTime != null) {
							$('#videosList').append("<div name=\"navProfile\" id=\"" + videos[i].videoId + "\" class=\"d-flex flex-column\"><button name=\"tooltip\" id=\"" + videos[i].videoId + "\" type=\"button\" class=\"btn btn-style\" style=\"padding:0px 0px 0px 0px\" data-bs-toggle=\"tooltip\" data-bs-html=\"true\" title=\"" + html + "\"><img src=\"" + videos[i].profilePath + "\" style=\"border-radius:50%; width:65px; border:2px red solid\"></button></div>");
						} else {
							$('#videosList').append("<div name=\"navProfile\" id=\"" + videos[i].videoId + "\" class=\"d-flex flex-column\"><button name=\"tooltip\" id=\"" + videos[i].videoId + "\" type=\"button\" class=\"btn btn-style\" style=\"padding:0px 0px 0px 0px\" data-bs-toggle=\"tooltip\" data-bs-html=\"true\" title=\"" + html + "\"><img src=\"" + videos[i].profilePath + "\" style=\"border-radius:50%; width:65px; border:2px green solid\"></button></div>");
						}
					}
				}
				$('[name="tooltip"]').each(function() {
					var tooltip = new bootstrap.Tooltip($(this), {
						boundary: document.body // or document.querySelector('#boundary')
					});
				});
			},
			error: function() {
				$('#videosList').append("<p>오류가 발생했습니다.</p>");
			}
		});
		$('#refresh').children('a').remove();
	}

	$('#refresh').click(function() {
		refresh();
	});

	var iframes = 1;

	function resize(width, height) {
		$('#iframeDiv').find('iframe').each(function() {
			$(this).width(width);
			$(this).height(height);
		});
	}


	$(document).on('click', '.btn-style', function() {
		var width = $('#iframeDiv').width();
		var height = $('#iframeDiv').height();
		var longest = width == Math.max(width, height) ? "width" : "height";

		var videoId = $(this).attr('id');

		var unique = true;
		$('#iframeDiv').find('iframe').each(function() {
			if ($(this).attr('id') == videoId)
				unique = false;
		});

		if (unique == true) {
			if (iframes == 1) {
				height = height - 22;
			}
			else if (iframes == 2) {
				if (longest == "width") {
				width = width / 2;
				height = height - 22;
			}
				else
					height = height / 2 - 22;
			}
			else if (iframes <= 4) {
				width = width / 2;
				height = height / 2 - 22;
			} else if (iframes <= 6) {
				if (longest == "width") {
					width = width / 3;
					height = height / 2 - 22;
				}
				else {
					width = width / 2;
					height = height / 3 - 22;
				}
			} else if (iframes <= 9) {
				width = width / 3;
				height = height / 3 - 22;
			} else {
				return;
			}

			iframes += 1;
			$('#iframeDiv').append("<div class=\"col\" style=\"margin:0px 0px 0px 0px; padding:0px 0px 0px 0px\"><div class=\"d-flex flex-column\"><div clas=\"d-flex\"><iframe id=\"" + videoId + "\" width=\"" + width + "\" height=\"" + height + "\" src=\"https://www.youtube.com/embed/" + $(this).attr('id') + "\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe></div><div class=\"d-flex justify-content-end\"><div class=\"d-flex\" style=\"font-size:0.9em\">↑ 해당 영상을 제거하려면 오른쪽 버튼 클릭 &nbsp;&nbsp; </div><div class=\"d-flex\"><button id=\"detach\" type=\"button\"><svg xmlns=\"http://www.w3.org/2000/svg\" width=\"16\" height=\"16\" fill=\"currentColor\" class=\"bi bi-trash\" viewBox=\"0 0 16 16\"> <path d=\"M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z\" /> <path fill-rule=\"evenodd\" d=\"M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z\" /> </svg></button></div><div></div></div>");
			resize(width, height);
			refresh();
		}
	});

	$(document).on('click', '#detach', function() {
		$(this).parent().closest('.col').remove();
		iframes -= 1;

		var width = $('#iframeDiv').width();
		var height = $('#iframeDiv').height();
		var longest = width == Math.max(width, height) ? "width" : "height";

		if (iframes == 2) {
			height = height - 22;
		}
		else if (iframes == 3) {
			if (longest == "width") {
				width = width / 2;
				height = height - 22;
			}
			else
				height = height / 2 - 22;
		}
		else if (iframes <= 5) {
			width = width / 2;
			height = height / 2 - 22;
		} else if (iframes <= 7) {
			if (longest == "width") {
				width = width / 3;
				height = height / 2 - 22;
			}
			else {
				width = width / 2;
				height = height / 3 - 22;
			}
		} else if (iframes <= 10) {
			width = width / 3;
			height = height / 3 - 22;
		}
		resize(width, height);
		refresh();
	});
});