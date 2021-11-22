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
	String apiKey;
	
	public VideoDTO(String memberName, String channelId, String videoId, String scheduledStartTime, String thumbnailPath, String apiKey) {
		this.memberName = memberName;
		this.channelId = channelId;
		this.videoId = videoId;
		this.scheduledStartTime = scheduledStartTime;
		this.thumbnailPath = thumbnailPath;
		this.apiKey = apiKey;
	}
}
