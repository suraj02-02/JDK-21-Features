package virtual_threads;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class PlatformVsVirtualThreadModelConcurrencyBenchMark {

    private static final AtomicInteger taskCounter = new AtomicInteger(0);
    private static final int platformThreadCount = 4000;
    private static final int virtualThreadCount = 4_000_000;

    public static void main(String[] args) throws InterruptedException {

        /**
         *  Here is a quick demonstration of how performant virtual thread model is over Platform thread model.
         *
         *  System configs :
         *  ---------------
         *  Runtime.getRuntime().availableProcessors() -> 11
         *  Runtime.getRuntime().freeMemory() -> Approx 290 MB
         *
         *  Observations :
         *  --------------
         *  1. We spawn around 4000 platform threads and each thread performed {@link #externalBlockingCall()}
         *
         *     - Increasing the platform thread count above 4100 resulted in OOM error
         *     - So , in general we can say using platform threads we were able to handle 4000 avg concurrent request.
         *
         *  2. We spwan around 1 Million virtual threads and each thread performed {@link #externalBlockingCall()}
         *
         *     - Even increasing virtual thread count to 6 Million , we didn't face OOM error but processing got slow.
         *     - Till 5 million virtual thread count the application ran smoothly.
         *     - So , in general a very large number of concurrent request can be served using Virtual Thread Model over Platform thread.
         *
         *
         *   Note : The actual benefit of Virtual Thread Model will be visible in I/O intensive task like we did in the demonstration.
         */


          // execute one threadSpinner function at a time.
          threadSpinner(Thread.ofPlatform() , platformThreadCount);
          //threadSpinner(Thread.ofVirtual() , virtualThreadCount);

          Thread.currentThread().join();
    }

    private static void threadSpinner(Thread.Builder threadBuilderType , int threadCount) throws InterruptedException {
        for (int i = 0; i < threadCount; i++) {
            threadBuilderType.start(PlatformVsVirtualThreadModelConcurrencyBenchMark::externalBlockingCall);
        }
    }

    /**
     * This acts as a external n/w call
     */
    private static void externalBlockingCall() {
        try {
            Thread.sleep(Duration.ofSeconds(1));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Task completed : " + taskCounter.incrementAndGet());
    }
}
