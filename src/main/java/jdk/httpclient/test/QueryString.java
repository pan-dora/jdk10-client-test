package jdk.httpclient.test;

import static jdk.incubator.http.HttpResponse.BodyHandler.asString;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Incident Report 9117860
 * This is a simple test to demonstrate httpclient handling of encoded query strings with reserved characters
 * server echoes decoded query string (incorrect behaviour)
 */
public class QueryString {
    public static void main(String[] args) throws Exception {
        String httpURI;
        LightWeightHttpServer.initServer();
        httpURI = LightWeightHttpServer.httproot + "echo/foo";
        String queryURI = URLEncoder.encode("?predicate=http://foo#bar", StandardCharsets.UTF_8.toString());
        String encodedQueryUri = httpURI + queryURI;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI(encodedQueryUri))
                                         .build();
        try {
            client.send(request, asString());
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
        LightWeightHttpServer.stop();
        System.out.println("OK");
    }
}
