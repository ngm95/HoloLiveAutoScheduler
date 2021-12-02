package com.hololive.livestream.DTO;

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
	String scheduledStartTime;
	String actualStartTime;
	String actualEndTime;
	String thumbnailPath;
	String profilePath;
}
