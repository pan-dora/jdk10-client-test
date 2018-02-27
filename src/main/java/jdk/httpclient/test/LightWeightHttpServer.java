package jdk.httpclient.test;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LightWeightHttpServer {

    static HttpServer httpServer;
    static ExecutorService executor;
    static int port;
    static int httpsport;
    static String httproot;

    public static void initServer() throws IOException {

        Logger logger = Logger.getLogger("com.sun.net.httpserver");
        ConsoleHandler ch = new ConsoleHandler();
        logger.setLevel(Level.ALL);
        ch.setLevel(Level.ALL);
        logger.addHandler(ch);

        InetSocketAddress addr = new InetSocketAddress(0);
        httpServer = HttpServer.create(addr, 0);
        if (httpServer instanceof HttpsServer) {
            throw new RuntimeException("should not be httpsserver");
        }

        HttpContext c3 = httpServer.createContext("/echo", new EchoHandler());

        executor = Executors.newCachedThreadPool();
        httpServer.setExecutor(executor);
        httpServer.start();

        port = httpServer.getAddress().getPort();
        System.out.println("HTTP server port = " + port);
        System.out.println("HTTPS server port = " + httpsport);
        httproot = "http://127.0.0.1:" + port + "/";
    }

    public static void stop() throws IOException {
        if (httpServer != null) {
            httpServer.stop(0);
        }
        if (executor != null) {
            executor.shutdownNow();
        }
    }
}

