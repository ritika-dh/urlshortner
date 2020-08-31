package org.ritika.main.java;

public class UrlData {
	private String originalUrl;
	private Long count;
	
	UrlData(String originalUrl) {
		this.originalUrl = originalUrl;
		count = 0l;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}
	
	
}
