$(document).ready(function() {
	$('#iframeDiv').height((window.innerHeight - 80));
	$('#iframeDiv').width((window.innerWidth));

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
					$('#videosList').append("<div id=\"" + videos[i].videoId + "\" class=\"d-flex flex-column\"><button name=\"tooltip\" id=\"" + videos[i].videoId + "\" type=\"button\" class=\"btn btn-style\" style=\"padding:0px 0px 0px 0px\" data-bs-toggle=\"tooltip\" data-bs-html=\"true\" title=\"" + html + "\"><img src=\"" + videos[i].profilePath + "\" style=\"border-radius:50%; width:65px; border:2px red solid\"></button></div>");
				} else {
					$('#videosList').append("<div id=\"" + videos[i].videoId + "\" class=\"d-flex flex-column\"><button name=\"tooltip\" id=\"" + videos[i].videoId + "\" type=\"button\" class=\"btn btn-style\" style=\"padding:0px 0px 0px 0px\" data-bs-toggle=\"tooltip\" data-bs-html=\"true\" title=\"" + html + "\"><img src=\"" + videos[i].profilePath + "\" style=\"border-radius:50%; width:65px; border:2px green solid\"></button></div>");
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

	$('#refresh').click(function() {
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
					var html = '<div class=\'d-flex flex-column\'><div class=\'d-flex flex-row\'><img src=\'' + videos[i].thumbnailPath + '\' width=\'100%\' height=\'100%\'></div><div class=\'d-flex flex-row\'>예정 시각 : ' + videos[i].scheduledStartTime + '</div></div>';
					if (videos[i].actualStartTime != null) {
						$('#videosList').append("<div id=\"" + videos[i].videoId + "\" class=\"d-flex flex-column\"><button name=\"tooltip\" id=\"" + videos[i].videoId + "\" type=\"button\" class=\"btn btn-style\" style=\"padding:0px 0px 0px 0px\" data-bs-toggle=\"tooltip\" data-bs-html=\"true\" title=\"" + html + "\"><img src=\"" + videos[i].profilePath + "\" style=\"border-radius:50%; width:65px; border:2px red solid\"></button></div>");
					} else {
						$('#videosList').append("<div id=\"" + videos[i].videoId + "\" class=\"d-flex flex-column\"><button name=\"tooltip\" id=\"" + videos[i].videoId + "\" type=\"button\" class=\"btn btn-style\" style=\"padding:0px 0px 0px 0px\" data-bs-toggle=\"tooltip\" data-bs-html=\"true\" title=\"" + html + "\"><img src=\"" + videos[i].profilePath + "\" style=\"border-radius:50%; width:65px; border:2px green solid\"></button></div>");
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
	});

	var iframes = 1;
	$(document).on('click', '.btn-style', function() {
		var width = $('#iframeDiv').width();
		var height = $('#iframeDiv').height();

		var videoId = $(this).attr('id');
		var unique = true;

		console.log(unique);

		if (unique == true && iframes <= 4) {

			if (iframes == 1)
				;
			else if (iframes == 2) {
				width = width / 2;
			}
			else {
				width = width / 2;
				height = height / 2;
			}

			var input = "";
			$('#iframeDiv').find('iframe').each(function() {
				$(this).width(width);
				$(this).height(height);
			});

			input += "<div class=\"col\" style=\"margin:0px 0px 0px 0px; padding:0px 0px 0px 0px\"><iframe width=\"" + width + "\" height=\"" + height + "\" src=\"https://www.youtube.com/embed/" + $(this).attr('id') + "\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe></div>";
			iframes += 1;

			$('#iframeDiv').append(input);
		} else {
			$('#iframeDiv').children(':first').remove();
			$('#iframeDiv').append("<div class=\"col\" style=\"margin:0px 0px 0px 0px; padding:0px 0px 0px 0px\"><iframe width=\"" + width / 2 + "\" height=\"" + height / 2 + "\" src=\"https://www.youtube.com/embed/" + $(this).attr('id') + "\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe></div>");
		}
	});
});