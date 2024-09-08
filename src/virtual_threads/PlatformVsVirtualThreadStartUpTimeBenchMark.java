package virtual_threads;

import java.time.Duration;
import java.time.Instant;

public class PlatformVsVirtualThreadStartUpTimeBenchMark {

    public static void main(String[] args) {

        /**
         *  StartUp time comparison b/w platform and virtual threads
         *
         *  System configs :
         *  ---------------
         *   Runtime.getRuntime().availableProcessors() -> 11
         *   Runtime.getRuntime().freeMemory() -> Approx 290 MB
         *
         *   Observations :
         *   --------------
         *   1. On Avg 1 Million Platform threads took -> 35 seconds
         *   2. On Avg 1 Million Virtual threads took -> 1 - 2 seconds
         *
         *   This huge is mainly due to the difference in thread modeling.
         *   - The virtual threads creation is just a normal java object which is then  assigned to a ForkJoin pool platform
         *     thread for task processing by JVM.
         *
         *   - The platform threads are resource intensive having a 1::1 relation with kernel thread thus hardware and resource
         *     alignment takes most of the time making.
         */

         // execute one threadStartUp func at a time
        threadStartUp(Thread.ofPlatform() , 1_000_000);
        //threadStartUp(Thread.ofVirtual() , 5_000_000);
    }

    private static void threadStartUp(Thread.Builder threadBuilderType , int threadCount) {

        final Instant now = Instant.now();
        for (int i = 0; i < threadCount; i++) {
            threadBuilderType.start(() -> System.out.println("Thread Info :" + Thread.currentThread()));
        }
        System.out.println("Time took : " + Duration.between(now , Instant.now()).getSeconds());
    }
}
