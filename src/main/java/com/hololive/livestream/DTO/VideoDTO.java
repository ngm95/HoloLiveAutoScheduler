package com.hololive.livestream.DTO;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoDTO {
	String memberName;
	String channelId;
	String videoId;
	Date scheduledStartTime;
	Date actualStartTime;
	Date actualEndTime;
	String profilePath;
}
