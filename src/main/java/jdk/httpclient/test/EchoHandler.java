package jdk.httpclient.test;

import com.sun.net.httpserver.*;
import java.io.*;

public class EchoHandler implements HttpHandler {
    public EchoHandler() {}

    @Override
    public void handle(HttpExchange t)
            throws IOException {
        try {
            System.err.println("EchoHandler received request to " + t.getRequestURI());
            InputStream is = t.getRequestBody();
            Headers map = t.getRequestHeaders();
            Headers map1 = t.getResponseHeaders();
            map1.add("X-Hello", "world");
            map1.add("X-Bye", "universe");
            String fixedrequest = map.getFirst("XFixed");
            File outfile = File.createTempFile("foo", "bar");
            FileOutputStream fos = new FileOutputStream(outfile);
            int count = (int) is.transferTo(fos);
            is.close();
            fos.close();
            InputStream is1 = new FileInputStream(outfile);
            OutputStream os = null;
            // return the number of bytes received (no echo)
            String summary = map.getFirst("XSummary");
            if (fixedrequest != null && summary == null) {
                t.sendResponseHeaders(200, count);
                os = t.getResponseBody();
                is1.transferTo(os);
            } else {
                t.sendResponseHeaders(200, 0);
                os = t.getResponseBody();
                is1.transferTo(os);

                if (summary != null) {
                    String s = Integer.toString(count);
                    os.write(s.getBytes());
                }
            }
            outfile.delete();
            os.close();
            is1.close();
        } catch (Throwable e) {
            e.printStackTrace();
            throw new IOException(e);
        }
    }
}

