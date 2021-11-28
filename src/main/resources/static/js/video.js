$(document).ready(function() {
	var token = $("meta[name='_csrf_token']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	
	$('#iframeDiv').height((window.innerHeight - 80));
	$('#iframeDiv').width((window.innerWidth));
	
	var sizeArr = [[[24, 24]], [[12, 24], [12, 24]], [[12, 24], [12, 12], [12, 12]], [[12, 12], [12, 12], [12, 12], [12, 12]], [[12, 15], [12, 15], [8, 9], [8, 9], [8, 9]], [[8, 12], [8, 12], [8, 12], [8, 12], [8, 12], [8, 12]],
					[[9, 12], [9, 12], [9, 12], [9, 12], [6, 8], [6, 8], [6, 8]], [[10, 12], [10, 12], [7, 8], [7, 8], [7, 8], [7, 8], [7, 8], [7, 8]], [[8, 8], [8, 8], [8, 8], [8, 8], [8, 8], [8, 8], [8, 8], [8, 8], [8, 8]]];
	var posArr = [[[0, 0]], [[0, 0], [12, 0]], [[0, 0], [12, 0], [12, 12]], [[0, 0], [12, 0], [0, 12], [12, 12]], [[0, 0], [12, 0], [0, 15], [8, 15], [16, 15]], [[0, 0], [8, 0], [16, 0], [0, 12], [8, 12], [16, 12]], 
					[[0, 0], [9, 0], [0, 12], [9, 12], [18, 0], [18, 8], [18, 16]], [[0, 0], [0, 12], [10, 0], [17, 0], [10, 8], [17, 8], [10, 16], [17, 16]], [[0, 0], [8, 0], [16, 0], [0, 8], [8, 8], [16, 8], [0, 16], [8, 16], [16, 16]]];
	
	
	var videos = 0;
	

	var subWidth = 2;
	var subHeight = 28;
	
//	function setResizeSensor(resizeDiv) {
//		new ResizeSensor(resizeDiv, function(size) {
//			console.log(resizeDiv.attr('id') + " : " + size.width + ', ' + size.height);
//			resizeDiv.find('iframe').width(size.width-subWidth);
//			resizeDiv.find('iframe').height(size.height-subHeight);
//			resizeDiv.attr('id', 'resized');
//		});
//	}

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
	
	function resize() {
		var width = $('#iframeDiv').width()/24;
		var height = $('#iframeDiv').height()/24;
		var longest = width == Math.max(width, height) ? "width" : "height";
		var index = 0;
		
		$('#iframeDiv').children('#video').each(function() {
			if (longest == "width") {
				sizeInfo = sizeArr[videos-1][index];
				var divWidth = width*sizeInfo[0]-subWidth;
				var divHeight = height*sizeInfo[1]-subHeight;
				console.log($('#iframeDiv').width() + ", " + $('#iframeDiv').height() + " + " + sizeInfo[0] + ", " + sizeInfo[1] + " -> " + divWidth + ', ' + divHeight);
				$(this).find('iframe').width(divWidth);
				$(this).find('iframe').height(divHeight);
				
				posInfo = posArr[videos-1][index];
				var left = width*posInfo[0];
				var top = height*posInfo[1];
				$(this).attr('style', 'position:absolute; margin:0px 0px 0px 0px; padding:0px 0px 0px 0px; width:' + divWidth + 'px; height:' + divHeight + 'px; left:' + left + "px; top:" + top + "px");
			}
			else {
				sizeInfo = sizeArr[videos-1][index];
				var divWidth = width*sizeInfo[1]-subWidth;
				var divHeight = height*sizeInfo[0]-subHeight;
				console.log($('#iframeDiv').width() + ", " + $('#iframeDiv').height() + " + " + sizeInfo[0] + ", " + sizeInfo[1] + " -> " + divWidth + ', ' + divHeight);
				$(this).find('iframe').width(divWidth);
				$(this).find('iframe').height(divHeight);
				
				posInfo = posArr[videos-1][index];
				var left = width*posInfo[1];
				var top = height*posInfo[0];
				$(this).attr('style', 'position:absolute; margin:0px 0px 0px 0px; padding:0px 0px 0px 0px; width:' + divWidth + 'px; height:' + divHeight + 'px; left:' + left + "px; top:" + top + "px");
			}
			
			index += 1;
		});
	}
	
	$(window).resize(function() {
		$('#iframeDiv').height((window.innerHeight - 80));
		$('#iframeDiv').width((window.innerWidth))
		
		resize();
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
	
	var myModal = new bootstrap.Modal(document.getElementById('noticeModal'), {
		keyboard: false
	});
	
	function showNoticeModal() {
		$('#modalBody').empty();
		$('#modalBody').append('이미 9개의 방송이 켜져 있습니다.');
		myModal.show();
	}

	$(document).on('click', '[name="tooltip"]', function() {
		var videoId = $(this).attr('id');

		var unique = true;
		$('#iframeDiv').find('iframe').each(function() {
			if ($(this).attr('id') == videoId)
				unique = false;
		});

		if (unique == true) {
			if (videos >= 9) {
				showNoticeModal();
				return;
			}
			
			$('#iframeDiv').append("<div id=\"video\"><div class=\"d-flex flex-column\" style=\"border:1px gray solid; border-radius:5px;\"><div clas=\"d-flex\"><iframe id=\"" + videoId + "\" width=\"" + $('#iframeDiv').width()/24 + "\" height=\"" + $('#iframeDiv').height()/24 + "\" src=\"https://www.youtube.com/embed/" + videoId + "\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe></div><div class=\"d-flex justify-content-end\"><div class=\"d-flex\" style=\"font-size:1.0em\">↑ 영상 제거 : &nbsp;</div><div class=\"d-flex\"><button class=\"btn btn-sm btn-outline-secondary\" id=\"detach\" type=\"button\"><svg xmlns=\"http://www.w3.org/2000/svg\" width=\"16\" height=\"16\" fill=\"currentColor\" class=\"bi bi-x-circle-fill\" viewBox=\"0 0 16 16\"><path d=\"M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zM5.354 4.646a.5.5 0 1 0-.708.708L7.293 8l-2.647 2.646a.5.5 0 0 0 .708.708L8 8.707l2.646 2.647a.5.5 0 0 0 .708-.708L8.707 8l2.647-2.646a.5.5 0 0 0-.708-.708L8 7.293 5.354 4.646z\"/></svg></button></div><div></div></div>");
//			var added = $('#iframeDiv').children(':last');
//			setResizeSensor(added);

			videos += 1;
			resize();
			refresh();
		} else {
			showNoticeModal();
		}
	});

	$(document).on('click', '#detach', function() {
		$(this).parent().closest('#video').remove();
		
		videos -= 1;
		
		resize();
		refresh();
	});
	
	$(document).on('click', '[name="select"]', function() {
		if (videos >= 9) {
			showNoticeModal();
		}
		else {
			var videoId = $(this).attr('id');
			$('#iframeDiv').append("<div id=\"video\"><div class=\"d-flex flex-column\" style=\"border:1px gray solid; border-radius:5px;\"><div clas=\"d-flex\"><iframe id=\"" + videoId + "\" width=\"" + $('#iframeDiv').width()/24 + "\" height=\"" + $('#iframeDiv').height()/24 + "\" src=\"https://www.youtube.com/embed/" + videoId + "\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe></div><div class=\"d-flex justify-content-end\"><div class=\"d-flex\" style=\"font-size:1.0em\">↑ 영상 제거 : &nbsp;</div><div class=\"d-flex\"><button class=\"btn btn-sm btn-outline-secondary\" id=\"detach\" type=\"button\"><svg xmlns=\"http://www.w3.org/2000/svg\" width=\"16\" height=\"16\" fill=\"currentColor\" class=\"bi bi-x-circle-fill\" viewBox=\"0 0 16 16\"><path d=\"M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zM5.354 4.646a.5.5 0 1 0-.708.708L7.293 8l-2.647 2.646a.5.5 0 0 0 .708.708L8 8.707l2.646 2.647a.5.5 0 0 0 .708-.708L8.707 8l2.647-2.646a.5.5 0 0 0-.708-.708L8 7.293 5.354 4.646z\"/></svg></button></div><div></div></div>");
			
			videos += 1;
			resize();
		}
		
		refresh();
	});
});