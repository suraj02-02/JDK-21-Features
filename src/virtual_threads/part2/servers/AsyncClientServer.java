package virtual_threads.part2.servers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AsyncClientServer {

    /*** Provide the below URI based on you test ********/
    private static final String PLATFORM_THREAD_URI = "http://localhost:9000/platformthread/execute";
    private static final String VIRTUAL_THREAD_URI = "http://localhost:8000/virtualthread/execute";

    /********** Request parameters and executor  *************/
    private static final int NUM_REQUESTS = 10_000; // adjust this to simulate the desired load
    private static final int CONCURRENCY_LEVEL = Runtime.getRuntime().availableProcessors() ; // adjust this to simulate the desired concurrency
    static ExecutorService executor = Executors.newFixedThreadPool(CONCURRENCY_LEVEL);

    /**
     * Method will be executing the <code>{@link #sendRequest() }</code> method in parallel for <code>{@link #NUM_REQUESTS}</code> times.
     * @param args
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException {

        Instant now = Instant.now();
        for (int i = 0; i < NUM_REQUESTS; i++) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    try {
                        sendRequest();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } catch (IOException e) {
                    System.err.println("Error sending request: " + e.getMessage());
                }
            }, executor);
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("Load test completed in " + Duration.between(Instant.now() , now).getSeconds() + " seconds");
    }

    /**
     * Method will send a request to the provided URI.
     * @throws IOException
     * @throws InterruptedException
     */
    private static void sendRequest() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRTUAL_THREAD_URI))
                .POST(HttpRequest.BodyPublishers.ofString("{\"key\": \"value\"}"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Received response: " + response.body());
    }
}