package virtual_threads.part2.servers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import virtual_threads.part2.utils.DataUploader;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *  <p>
 *    This is a HTTP based server which will be accepting request from clients and executing each request in a separate <code>Platform Thread</code>
 *     using {@link Executors#newFixedThreadPool(int)} )}
 *  </p>
 *  <p>
 *      We will be load testing this server using a client which will be posting requests to this server .
 *  </p>
 */

public class PlatformThreaded_HttpServer {

    private static final int SERVER_PORT = 9000;
    public static final String SERVER_CONTEXT_PATH = "/platformthread/execute";
    private static final int ACTIVE_QUEUED_CONNECTIONS_IN_BACKLOGS = 5;
    private static final int THREAD_COUNT = 500;
    private static final String FILE_NAME = "Platform-Thread-Data.txt";
    static AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) throws IOException {

        HttpServer httpServer = HttpServer.create(new InetSocketAddress(SERVER_PORT) ,  ACTIVE_QUEUED_CONNECTIONS_IN_BACKLOGS);
        httpServer.createContext(SERVER_CONTEXT_PATH, PlatformThreaded_HttpServer::taskTobeExecuted);
        // Setting up Fixed Thread Pool Executor
        httpServer.setExecutor(Executors.newFixedThreadPool(THREAD_COUNT));
        httpServer.start();
        System.out.println("Server has been Started on port : " + SERVER_PORT);
    }

    /**
     *  Method will be invoked whenever the server recieves a new request for the <code>contextPathURI  </code> :  {@link #SERVER_CONTEXT_PATH}
     *
     * @param httpExchange
     * @throws IOException
     */
    private static void taskTobeExecuted(HttpExchange httpExchange) throws IOException {

        System.out.println("Request Count : " + counter.incrementAndGet()    +  " Thread Name : " + Thread.currentThread().getName());
        String response = "Message : has been recieved and was executed by thread : " + Thread.currentThread();
        httpExchange.sendResponseHeaders(200 , response.length());
        // Upload Data to disk
        DataUploader.uploadData(httpExchange.getRequestBody() , FILE_NAME);
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
    }

}
