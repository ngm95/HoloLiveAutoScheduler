package com.hololive.livestream.DTO;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoAPI {
	String memberName;
	String id;
	String title;
	String type;
	String topic_id;
	String status;
	Date published_at;
	Date available_at;
	Date start_scheduled;
	Date start_actual;
	Date end_actual;
	int duration;
	int live_viewers;
	String description;
	ChannelDTO channel;

	public void setPublished_at(String published_at) throws ParseException {
		if (published_at != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			sdf.setTimeZone(TimeZone.getTimeZone("KST"));
			this.published_at = sdf.parse(published_at);
		}
	}

	public void setAvailable_at(String available_at) throws ParseException {
		if (available_at != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			sdf.setTimeZone(TimeZone.getTimeZone("KST"));
			this.available_at = sdf.parse(available_at);
		}
	}

	public void setStart_scheduled(String start_scheduled) throws ParseException {
		if (start_scheduled != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			sdf.setTimeZone(TimeZone.getTimeZone("KST"));
			this.start_scheduled = sdf.parse(start_scheduled);
		}
	}

	public void setStart_actual(String start_actual) throws ParseException {
		if (start_actual != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			sdf.setTimeZone(TimeZone.getTimeZone("KST"));
			this.start_actual = sdf.parse(start_actual);
		}
	}

	public void setEnd_actual(String end_actual) throws ParseException {
		if (end_actual != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			sdf.setTimeZone(TimeZone.getTimeZone("KST"));
			this.end_actual = sdf.parse(end_actual);
		}
	}
}
