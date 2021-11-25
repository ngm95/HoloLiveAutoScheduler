$(document).ready(function() {
	var token = $("meta[name='_csrf_token']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	
	$('#iframeDiv').height((window.innerHeight - 80));
	$('#iframeDiv').width((window.innerWidth));

	var iframes = 0;

	var subWidth = 2;
	var subHeight = 28;

	function getIframeWidth() {
		var width = $('#iframeDiv').width();
		var height = $('#iframeDiv').height();
		var longest = width == Math.max(width, height) ? "width" : "height";

		if (iframes == 1) {
			return width - subWidth;
		}
		else if (iframes == 2) {
			if (longest == "width")
				return width / 2 - subWidth;
			else
				return width - subWidth;
		}
		else if (iframes <= 4) {
			return width / 2 - subWidth;
		} 
		else if (iframes <= 6) {
			if (longest == "width")
				return width / 3 - subWidth;
			else
				return width / 2 - subWidth;
		} 
		else {
			return width / 3 - subWidth;
		}
	}

	function getIframeHeight() {
		var width = $('#iframeDiv').width();
		var height = $('#iframeDiv').height();
		var longest = width == Math.max(width, height) ? "width" : "height";

		if (iframes == 1) {
			return height - subHeight;
		}
		else if (iframes == 2) {
			if (longest == "width")
				return height - subHeight;
			else
				return height / 2 - subHeight;
		}
		else if (iframes <= 4) {
			return height / 2 - subHeight;
		} 
		else if (iframes <= 6) {
			if (longest == "width")
				return height / 2 - subHeight;
			else
				return height / 3 - subHeight;
		} 
		else {
			return height / 3 - subHeight;
		}
	}

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
	
	function resize(width, height) {
		$('#iframeDiv').find('iframe').each(function() {
			$(this).width(width);
			$(this).height(height);
		});
	}
	
	$(window).resize(function() {
		$('#iframeDiv').height((window.innerHeight - 80));
		$('#iframeDiv').width((window.innerWidth))
		
		var width = getIframeWidth();
		var height = getIframeHeight();
		
		resize(width, height);
	});

	$('#refresh').click(function() {
		refresh();
	});

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

	$(document).on('click', '.btn-style', function() {
		var videoId = $(this).attr('id');

		var unique = true;
		$('#iframeDiv').find('iframe').each(function() {
			if ($(this).attr('id') == videoId)
				unique = false;
		});

		if (unique == true) {
			if (iframes > 9) {
				return;
			}
			
			iframes += 1;
			var width = getIframeWidth();
			var height = getIframeHeight();

			
			$('#iframeDiv').append("<div class=\"col\" style=\"margin:0px 0px 0px 0px; padding:0px 0px 0px 0px\"><div class=\"d-flex flex-column\" style=\"border:1px gray solid; border-radius:5px;\"><div clas=\"d-flex\"><iframe id=\"" + videoId + "\" width=\"" + width + "\" height=\"" + height + "\" src=\"https://www.youtube.com/embed/" + $(this).attr('id') + "\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe></div><div class=\"d-flex justify-content-end\"><div class=\"d-flex\" style=\"font-size:1.0em\">↑ 영상 제거 : &nbsp;</div><div class=\"d-flex\"><button class=\"btn btn-sm btn-outline-secondary\" id=\"detach\" type=\"button\"><svg xmlns=\"http://www.w3.org/2000/svg\" width=\"16\" height=\"16\" fill=\"currentColor\" class=\"bi bi-x-circle-fill\" viewBox=\"0 0 16 16\"><path d=\"M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zM5.354 4.646a.5.5 0 1 0-.708.708L7.293 8l-2.647 2.646a.5.5 0 0 0 .708.708L8 8.707l2.646 2.647a.5.5 0 0 0 .708-.708L8.707 8l2.647-2.646a.5.5 0 0 0-.708-.708L8 7.293 5.354 4.646z\"/></svg></button></div><div></div></div>");
			resize(width, height);
			refresh();
		}
	});

	$(document).on('click', '#detach', function() {
		$(this).parent().closest('.col').remove();
		
		iframes -= 1;
		var width = getIframeWidth();
		var height = getIframeHeight();
		
		resize(width, height);
		refresh();
	});
});