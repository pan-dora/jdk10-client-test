package jdk.httpclient.test;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

     /**
     * Incident Report 9117860
     * This is a simple test to demonstrate httpclient handling of encoded query strings with reserved characters
     * server echoes encoded query string
     */
    public class QueryStringWSClient {
        public static void main(String[] args) throws Exception {
            String httpURI;
            LightWeightHttpServer.initServer();
            httpURI = LightWeightHttpServer.httproot + "echo/foo";
            String queryURI = URLEncoder.encode("?predicate=http://foo#bar", StandardCharsets.UTF_8.toString());
            String encodedQueryUri = httpURI + queryURI;
            Client client = ClientBuilder.newClient();
            try {
                WebTarget target = client.target(new URI(encodedQueryUri));
                final Response res = target.request().get();
            } catch (Throwable e) {
                e.printStackTrace();
                throw e;
            }
            LightWeightHttpServer.stop();
            System.out.println("OK");
        }
    }

