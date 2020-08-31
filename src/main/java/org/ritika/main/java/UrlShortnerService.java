package org.ritika.main.java;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UrlShortnerService {
	
	static UrlShortnerService instance = new UrlShortnerService();
	Map<Long, User> userMap;
	Map<String, UrlData> shortUrlMap;
	BigInteger idCounter; 
	static final BigInteger zero = new BigInteger("0");
	static final BigInteger ten = new BigInteger("10");
	static final String map = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	
	public UrlShortnerService() {
		userMap = new HashMap<Long, User>();
		shortUrlMap = new ConcurrentHashMap<String, UrlData>();
		idCounter = new BigInteger("0");
	}
	
	public static UrlShortnerService getInstance() {
		return instance;
	}
	
	public String getShortenedUrl(String longUrl, Long userId) {
		User user = userMap.get(userId);
		if(user == null) {
			synchronized(userMap) {
				if(!userMap.containsKey(userId)) {
					user = new User(userId);
					userMap.put(userId, user);
				}
			}
		}
		user = userMap.get(userId);
		if(user.getUrlMap().containsKey(longUrl))
			return user.getUrlMap().get(longUrl);
		
		BigInteger n;
		/* some idCounters will be wasted in this approach when 2 or more same requests are made within very little
		time difference, we can avoid this by adding the code to put in map here, but this will limit concurrency,
		in case 2 different users make the request at the same time. We have chosen to go with wasting few ids */
		synchronized(idCounter) {
			n = idCounter;
			idCounter = idCounter.add(BigInteger.ONE);
		}
		String shortUrl = convertUniqueIdToString(n);
		Map<String, String> map = user.getUrlMap();
		synchronized(map) {
			if(map.containsKey(longUrl))
				return map.get(longUrl);
			map.put(longUrl, shortUrl);
			shortUrlMap.put(shortUrl, new UrlData(longUrl));
		}
		return shortUrl;		
	}
	
	public String getOriginalURL(String shortUrl) {
		if(!shortUrlMap.containsKey(shortUrl)) {
			return "";
		}	
		UrlData urlData = shortUrlMap.get(shortUrl);
		synchronized(urlData) {
			urlData.setCount(urlData.getCount() + 1l);
		}	
		return urlData.getOriginalUrl();
	}
	
	public Long getHitCount(String shortUrl) {
		if(!shortUrlMap.containsKey(shortUrl))
			return -1l;
		return shortUrlMap.get(shortUrl).getCount();
	}	
	
	private String convertUniqueIdToString(BigInteger n) {
		StringBuilder shortUrl = new StringBuilder();
		BigInteger[] temp = new BigInteger[2];
		while(n.compareTo(zero) != 0) {
			temp = n.divideAndRemainder(ten);
			int index = temp[1].intValue();
			shortUrl = shortUrl.append(map.charAt(index));
			n = temp[0];
		}
		int size = shortUrl.length();
		while(size < 8) {
			shortUrl.append(map.charAt(0));
			size++;
		}
		return shortUrl.reverse().toString();
	}
	
	

}
