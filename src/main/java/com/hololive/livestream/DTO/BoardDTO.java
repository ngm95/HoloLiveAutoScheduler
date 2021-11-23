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
}
