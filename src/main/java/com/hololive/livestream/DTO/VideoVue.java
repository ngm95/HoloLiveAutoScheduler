package com.hololive.livestream.DTO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoVue {
	String memberName;
	String channelId;
	String videoId;
	String scheduledStartTime;
	String actualStartTime;
	String actualEndTime;
	String profilePath;

	public void setScheduledStartTime(Date scheduledStartTime) {
		if (scheduledStartTime != null) {
			DateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
			this.scheduledStartTime = sdf.format(scheduledStartTime);
		}
	}

	public void setActualStartTime(Date actualStartTime) {
		if (actualStartTime != null) {
			DateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
			this.actualStartTime = sdf.format(actualStartTime);
		}
	}

	public void setActualEndTime(Date actualEndTime) {
		if (actualEndTime != null) {
			DateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
			this.actualEndTime = sdf.format(actualEndTime);
		}
	}
}
