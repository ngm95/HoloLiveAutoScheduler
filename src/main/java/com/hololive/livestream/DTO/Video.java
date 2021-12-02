package com.hololive.livestream.DTO;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Video {
	String memberName;
	String id;
	String title;
	String type;
	String topic_id;
	String status;
	String published_at;
	String available_at;
	String start_scheduled;
	String start_actual;
	String end_actual;
	int duration;
	int live_viewers;
	String description;
	ChannelDTO channel;

	public void setPublished_at(String published_at) throws ParseException {
		if (published_at != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			sdf.setTimeZone(TimeZone.getTimeZone("KST"));
			DateFormat format = new SimpleDateFormat("yy.MM.dd HH:mm");
			this.published_at = format.format(sdf.parse(published_at)).toString();
		}
	}

	public void setAvailable_at(String available_at) throws ParseException {
		if (available_at != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			sdf.setTimeZone(TimeZone.getTimeZone("KST"));
			DateFormat format = new SimpleDateFormat("yy.MM.dd HH:mm");
			this.available_at = format.format(sdf.parse(available_at)).toString();
		}
	}

	public void setStart_scheduled(String start_scheduled) throws ParseException {
		if (start_scheduled != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			sdf.setTimeZone(TimeZone.getTimeZone("KST"));
			DateFormat format = new SimpleDateFormat("yy.MM.dd HH:mm");
			this.start_scheduled = format.format(sdf.parse(start_scheduled)).toString();
		}
	}

	public void setStart_actual(String start_actual) throws ParseException {
		if (start_actual != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			sdf.setTimeZone(TimeZone.getTimeZone("KST"));
			DateFormat format = new SimpleDateFormat("yy.MM.dd HH:mm");
			this.start_actual = format.format(sdf.parse(start_actual)).toString();
		}
	}

	public void setEnd_actual(String end_actual) throws ParseException {
		if (end_actual != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			sdf.setTimeZone(TimeZone.getTimeZone("KST"));
			DateFormat format = new SimpleDateFormat("yy.MM.dd HH:mm");
			this.end_actual = format.format(sdf.parse(end_actual)).toString();
		}
	}
}
