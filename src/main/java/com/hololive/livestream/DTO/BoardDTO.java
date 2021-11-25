package com.hololive.livestream.DTO;

import lombok.Data;

@Data
public class BoardDTO {
	int boardId;
	String title;
	String videoId;
	String videoDetail; 
	int viewed;
	int love;
	int dislike;
	String date;
	
	public void setVideoIdWithShortPath(String shortPath) {
		this.videoId = shortPath.substring(17);
	}
	
	public void setVideoIdWithLongPath(String longPath) {
		this.videoId = longPath.substring(32);
	}
}
