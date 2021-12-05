$(document).ready(function() {
	$('#findVideo').append('<button type=\"button\" class=\"btn btn-secondary\" data-bs-toggle=\"modal\" data-bs-target=\"#findVideoModal\" style=\"margin-right:10px; height:47px\"><svg xmlns=\"http://www.w3.org/2000/svg\" width=\"16\" height=\"16\" fill=\"currentColor\" class=\"bi bi-plus-lg\" viewBox=\"0 0 16 16\"><path fill-rule=\"evenodd\" d=\"M8 2a.5.5 0 0 1 .5.5v5h5a.5.5 0 0 1 0 1h-5v5a.5.5 0 0 1-1 0v-5h-5a.5.5 0 0 1 0-1h5v-5A.5.5 0 0 1 8 2Z\"/></svg></button>');

	var select = 'live';
	
	
	showLive();
	showUpcoming();
	showCompleted();
	
	$(document).on('click', '#findVideo', function() {
		if (select == 'live') {
			console.log(select);
			$('#liveRadio').attr('checked', true);
			$('#upcomingRadio').attr('checked', false);
			$('#completedRadio').attr('checked', false);
			$('#modalUpcomingList').hide();
			$('#modalLiveList').show();
			$('#completedList').hide();
		}
		else if (select == 'upcoming') {
			console.log(select);
			$('#liveRadio').attr('checked', false);
			$('#upcomingRadio').attr('checked', true);
			$('#completedRadio').attr('checked', false);
			$('#modalUpcomingList').show();
			$('#modalLiveList').hide();
			$('#completedList').hide();
		}
		else if (select == 'completed') {
			console.log(select);
			$('#liveRadio').attr('checked', false);
			$('#upcomingRadio').attr('checked', false);
			$('#completedRadio').attr('checked', true);
			$('#modalUpcomingList').hide();
			$('#modalLiveList').hide();
			$('#completedList').show();
		}
	});
	
	function showLive() {
		$.ajax({
			url: "/livestream/getLiveList",
			method: "get",
			dataType: "json",
			contentType: "application/json;charset=UTF-8",
			success: function(videos) {
				$('#modalLiveList').empty();
				for (var i in videos) {
					$('#modalLiveList').append("<div id=\"" + videos[i].videoId + "\" name=\"select\" class=\"col\" data-bs-dismiss=\"modal\" aria-label=\"Close\" style=\"margin-bottom: 15px; padding-left: 8px; pdding-right: 8px\"><div class=\"d-flex\"><div class=\"card\" style=\"border-width: thick; border-color: red; width: 130px; height: 140.5px\"><div class=\"d-flex flex-column\"><div class=\"d-flex justify-content-between align-items-center\"><div class=\"d-flex flex-row\"><div class=\"d-flex\"><img src=\"" + videos[i].profilePath + "\" style=\"border-radius: 50%; width: 50px; height:50px; border: 1px white solid\"></div></div><div class=\"d-flex flex-column\"><div class=\"d-flex\" style=\"margin-top: 8px\"><b>" + videos[i].actualStartTime.substr(0, 8) + "</b></div><div class=\"d-flex\" style=\"margin-top: 8px\"><b>" + videos[i].actualStartTime.substr(9, 5) + "</b></div></div></div><div class=\"d-flex\"><img src=\"" + videos[i].thumbnailPath + "\" style=\"width: 120px; height: 67.5px;\"></div></div></div></div></div>");
				}
			},
			error: function() {
				$('#modalLiveList').append("<p>오류가 발생했습니다.</p>");
			}
		});
	}

	function showUpcoming() {
		$.ajax({
			url: "/livestream/getUpcomingList",
			method: "get",
			dataType: "json",
			contentType: "application/json;charset=UTF-8",
			success: function(videos) {
				$('#modalUpcomingList').empty();
				for (var i in videos) {
					$('#modalUpcomingList').append("<div id=\"" + videos[i].videoId + "\" name=\"select\" class=\"col\" data-bs-dismiss=\"modal\" aria-label=\"Close\" style=\"margin-bottom: 15px; padding-left: 8px; pdding-right: 8px\"><div class=\"d-flex\"><div class=\"card\" style=\"border-width: thick; border-color: green; width: 130px; height: 140.5px\"><div class=\"d-flex flex-column\"><div class=\"d-flex justify-content-between align-items-center\"><div class=\"d-flex flex-row\"><div class=\"d-flex\"><img src=\"" + videos[i].profilePath + "\" style=\"border-radius: 50%; width: 50px; height:50px; border: 1px white solid\"></div></div><div class=\"d-flex flex-column\"><div class=\"d-flex\" style=\"margin-top: 8px\"><b>" + videos[i].scheduledStartTime.substr(0, 8) + "</b></div><div class=\"d-flex\" style=\"margin-top: 8px\"><b>" + videos[i].scheduledStartTime.substr(9, 5) + "</b></div></div></div><div class=\"d-flex\"><img src=\"" + videos[i].thumbnailPath + "\" style=\"width: 120px; height: 67.5px;\"></div></div></div></div></div>");
				}
			},
			error: function() {
				$('#modalUpcomingList').append("<p>오류가 발생했습니다.</p>");
			}
		});
	}

	function showCompleted() {
		$.ajax({
			url: "/livestream/getCompletedListIn3Day",
			method: "get",
			dataType: "json",
			contentType: "application/json;charset=UTF-8",
			success: function(videos) {
				$('#modalCompletedList').empty();
				for (var i in videos) {
					$('#modalCompletedList').append("<div id=\"" + videos[i].videoId + "\" name=\"select\" class=\"col\" data-bs-dismiss=\"modal\" aria-label=\"Close\" style=\"margin-bottom: 15px; padding-left: 8px; pdding-right: 8px\"><div class=\"d-flex\"><div class=\"card\" style=\"border-width: thick; border-color: gray; width: 130px; height: 140.5px\"><div class=\"d-flex flex-column\"><div class=\"d-flex justify-content-between align-items-center\"><div class=\"d-flex flex-row\"><div class=\"d-flex\"><img src=\"" + videos[i].profilePath + "\" style=\"border-radius: 50%; width: 50px; height:50px; border: 1px white solid\"></div></div><div class=\"d-flex flex-column\"><div class=\"d-flex\" style=\"margin-top: 8px\"><b>" + videos[i].actualStartTime.substr(0, 8) + "</b></div><div class=\"d-flex\" style=\"margin-top: 8px\"><b>" + videos[i].actualStartTime.substr(9, 5) + "</b></div></div></div><div class=\"d-flex\"><img src=\"" + videos[i].thumbnailPath + "\" style=\"width: 120px; height: 67.5px;\"></div></div></div></div></div>");
				}
			},
			error: function() {
				$('#modalCompletedList').append("<p>오류가 발생했습니다.</p>");
			}
		});
	}

	function showCompletedAfterSomeday(start, end) {
		$.ajax({
			url: "/livestream/getCompletedListBetweenSomeday/"+start+"/"+end,
			type: "get",
			dataType: "json",
			success: function(videos) {
				console.log(videos);
				$('#modalCompletedList').empty();
				for (var i in videos) {
					$('#modalCompletedList').append("<div id=\"" + videos[i].videoId + "\" name=\"select\" class=\"col\" data-bs-dismiss=\"modal\" aria-label=\"Close\" style=\"margin-bottom: 15px; padding-left: 8px; pdding-right: 8px\"><div class=\"d-flex\"><div class=\"card\" style=\"border-width: thick; border-color: gray; width: 130px; height: 140.5px\"><div class=\"d-flex flex-column\"><div class=\"d-flex justify-content-between align-items-center\"><div class=\"d-flex flex-row\"><div class=\"d-flex\"><img src=\"" + videos[i].profilePath + "\" style=\"border-radius: 50%; width: 50px; height:50px; border: 1px white solid\"></div></div><div class=\"d-flex flex-column\"><div class=\"d-flex\" style=\"margin-top: 8px\"><b>" + videos[i].actualStartTime.substr(0, 8) + "</b></div><div class=\"d-flex\" style=\"margin-top: 8px\"><b>" + videos[i].actualStartTime.substr(9, 5) + "</b></div></div></div><div class=\"d-flex\"><img src=\"" + videos[i].thumbnailPath + "\" style=\"width: 120px; height: 67.5px;\"></div></div></div></div></div>");
				}
			},
			error: function() {
				$('#modalCompletedList').append("<p>오류가 발생했습니다.</p>");
			}
		});
	}

	$(document).on('click', '#liveRadio', function() {
		$('#modalLiveList').show();
		$('#modalUpcomingList').hide();
		$('#completedList').hide();
		select = 'live';
	});

	$(document).on('click', '#upcomingRadio', function() {
		$('#modalLiveList').hide();
		$('#modalUpcomingList').show();
		$('#completedList').hide();
		select = 'upcoming';
	});

	$(document).on('click', '#completedRadio', function() {
		$('#modalLiveList').hide();
		$('#modalUpcomingList').hide();
		$('#completedList').show();
		select = 'completed';
	});
	
	var today = new Date();
	$('#start').datepicker({
		dateFormat:"y.mm.dd"
	});
	$('#start').datepicker('setDate', new Date(today.setDate(today.getDate()-3)));
	$('#end').datepicker({
		dateFormat:"y.mm.dd"
	});
	$('#end').datepicker('setDate', new Date());
	
	$('#start').on("propertychange change keyup paste input", function() {
		var start = $(this).val() + " 00:00";
		var end = $('#end').val() + " 23:59";
		$('#modalCompletedList').empty();
		showCompletedAfterSomeday(start, end);
	});
	$('#end').on("propertychange change keyup paste input", function() {
		var start = $('#start').val() + " 00:00";
		var end = $(this).val() + " 23:59";
		$('#modalCompletedList').empty();
		showCompletedAfterSomeday(start, end);
	});
	
	$('#refresh').click(function() {
		showLive();
		showUpcoming();
		
		var start = $('#start').val() + " 00:00";
		var end = $('#end').val() + " 23:59";
		showCompletedAfterSomeday(start, end);
	});
});