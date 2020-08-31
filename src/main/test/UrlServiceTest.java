package main.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import main.java.UrlShortnerService;

public class UrlServiceTest {
	
	@Test
	public void testGetShortenedUrl() {
		UrlShortnerService service = new UrlShortnerService();
		String s1 = service.getShortenedUrl("mn/jkl/mmx0847kkl", 100l);
		String s2 = service.getShortenedUrl("mn/jkl/mmx0847kkl", 100l);
		String s3 = service.getShortenedUrl("hj/abc/mmx0847kkm", 1002l);
		String s4 = service.getShortenedUrl("mn/jkl/mmx0847kkl", 1002l);
		
		assertEquals("aaaaaaaa", s1);
		assertEquals(s1, s2);
		assertEquals("aaaaaaab", s3);
		assertNotEquals(s1, s4);
	}
	
	@Test
	public void testGetShortenedUrlMultithreaded() {
		UrlShortnerService service = new UrlShortnerService();
		ExecutorService executorService = Executors.newCachedThreadPool();
		for(int i=0; i<100; i++) {
			if(i%2 == 0)
				executorService.execute(new Runnable() {
					public void run() {
						assertEquals("aaaaaaaa", service.getShortenedUrl("mn/jkl/mmx0847kkl", 100l));
					} 
				});
			else 
				executorService.execute(new Runnable() {
					public void run() {
						assertEquals("aaaaaaab", service.getShortenedUrl("mn/jkl/mmx0847kkl", 1002l));
					} 
				});
	    	
		}
		try {
			executorService.awaitTermination(5, TimeUnit.SECONDS);
			executorService.shutdownNow();
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	@Test
	public void testGetOriginalURL() {
		UrlShortnerService service = new UrlShortnerService();
		String url1 = "mn/jkl/mmx0847kkl";
		String url2 = "hj/abc/mmx0847kkm";
		String s1 = service.getShortenedUrl(url1, 100l);
		String s3 = service.getShortenedUrl(url2, 1002l);
		String s4 = service.getShortenedUrl(url1, 1002l);
		String s5 = service.getOriginalURL(s1);
		String s6 = service.getOriginalURL(s3);
		String s7 = service.getOriginalURL(s4);
		
		assertEquals(url1, s5);
		assertEquals(url2, s6);
		assertEquals(url1, s7);
		assertEquals("", service.getOriginalURL("new random url"));
	}
	
	@Test
	public void testGetHitCount() {
		UrlShortnerService service = new UrlShortnerService();
		ExecutorService executorService = Executors.newCachedThreadPool();
		String originalUrl = "mn/jkl/mmx0847kkl";
		String shortUrl1 = service.getShortenedUrl(originalUrl, 100l);
		String shortUrl2 = service.getShortenedUrl(originalUrl, 1002l);
		for(int i=0; i<100; i++) {
			if(i%2 == 0)
				executorService.execute(new Runnable() {
					public void run() {
						service.getOriginalURL(shortUrl1);
					} 
				});
			else 
				executorService.execute(new Runnable() {
					public void run() {
						service.getOriginalURL(shortUrl2);
					} 
				});
	    	
		}
		try {
			executorService.awaitTermination(5, TimeUnit.SECONDS);
			executorService.shutdownNow();
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
		
		assertEquals(50l, service.getHitCount(shortUrl1));
		assertEquals(50l, service.getHitCount(shortUrl2));
		System.out.println(service.getHitCount(shortUrl2));
		
	}
	
}
