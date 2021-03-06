$(document).ready(function() {
	$('#findVideo').append('<button type=\"button\" class=\"btn btn-secondary\" data-bs-toggle=\"modal\" data-bs-target=\"#findVideoModal\" style=\"margin-right:10px; height:57px\"><svg xmlns=\"http://www.w3.org/2000/svg\" width=\"16\" height=\"16\" fill=\"currentColor\" class=\"bi bi-plus-lg\" viewBox=\"0 0 16 16\"><path fill-rule=\"evenodd\" d=\"M8 2a.5.5 0 0 1 .5.5v5h5a.5.5 0 0 1 0 1h-5v5a.5.5 0 0 1-1 0v-5h-5a.5.5 0 0 1 0-1h5v-5A.5.5 0 0 1 8 2Z\"/></svg></button>');
	
	$('#liveBody').show();
	$('#upcomingBody').hide();
	$('#completedBody').hide();
	
	$(document).on('click', '#liveRadio', function() {
		$('#liveBody').show();
		$('#upcomingBody').hide();
		$('#completedBody').hide();
	});
	
	$(document).on('click', '#upcomingRadio', function() {
		$('#liveBody').hide();
		$('#upcomingBody').show();
		$('#completedBody').hide();
	});
	
	$(document).on('click', '#completedRadio', function() {
		$('#liveBody').hide();
		$('#upcomingBody').hide();
		$('#completedBody').show();
	});
});