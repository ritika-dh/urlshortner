package org.ritika.main.java;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class User {
	private final Long userId;
	private final Map<String, String> urlMap;
	
	User(Long userId) {
		this.userId = userId;
		urlMap = new ConcurrentHashMap<String, String>();
	}
	
	public Long getUserId() {
		return userId;
	}
	public Map<String, String> getUrlMap() {
		return urlMap;
	}

}
