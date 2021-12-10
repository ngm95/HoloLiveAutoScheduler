package com.hololive.livestream.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoSearch {
	String start;
	String end;
	int offset;
	
	public VideoSearch(String start, String end) {
		this.start = start;
		this.end = end;
	}
}
