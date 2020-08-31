package org.ritika.main.java;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("urlShortner/urls")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UrlApi {
	
	static UrlShortnerService service = UrlShortnerService.getInstance();

    @POST
    @Path("longUrls/{longUrl}")
    public String getShortenedUrl(@PathParam("longUrl") String longUrl, Long userId) {
        return service.getShortenedUrl(longUrl, userId);
    }
    
    @GET
    @Path("shortUrls/{shortUrl}")
    public Response getOriginalUrl(@PathParam("shortUrl") String shortUrl) {
        String originalUrl = service.getOriginalURL(shortUrl);
        int status = originalUrl.isEmpty() ? 400 : 307;
        return Response.status(status)
        		.entity(originalUrl)
        		.build();     	
    }
    
    @GET
    @Path("shortUrls/{shortUrl}/hitCount")
    public Long getHitCount(@PathParam("shortUrl") String shortUrl) {
        return service.getHitCount(shortUrl);
    }
}
