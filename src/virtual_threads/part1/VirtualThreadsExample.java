package virtual_threads.part1;

/**
 * <b>
 * **** Java Virtual Threads: Project Loom ******
 * </b>
 *
 * <p>
 * Virtual Threads is the most important feature released with JDK-21 under project loom.
 * </p>
 * <p>
 * <b>### Why Virtual Threads ? </b>
 * </p>
 * <p>
 * As of the traditional thread model , For every new request made to a server a new thread (Platform thread) is created which is
 * directly mapped to an OS level thread. Thus there is a certain limitation during the creation of platform thread since they are resource  intensive.
 * </p>
 * <p>
 * Also the major problem with above traditional thread model is that during a blocking operation ex :
 *     <ul>
 *         <li>
 *             <code>Database call</code>
 *         </li>
 *         <li>
 *             <code>Waiting for response from external Api's</code>
 *         </li>
 *     </ul>
 *     the platform thread will be sitting idle as a result of which the mapped OS thread will also be sitting idle
 *     till the operation resumes. This leads to under utilization of server resources impacting it's scalability.
 * </p>
 * <p>
 *    This pattern limits the throughput of the server because the number of concurrent requests (that server can handle) becomes directly proportional
 *    to the server’s hardware performance. So, the number of available threads has to be limited even in multi-core processors.
 * </p>
 * <p>
 *    <b>### How Virtual Threads solves resource utilization problem ?</b>
 * </p>
 * <p>
 *   <code>Virtual</code> threads are also an instance of java.lang.Thread that runs its code on underlying OS threads but the main thing is it does not
 *   block the OS thread for it's entire lifetime unlike the <code>platform</code> threads.
 * </p>
 * <p>
 *   Millions of <code>virtual</code> threads can be created without depending on the number of platform threads. Unlike the platform threads these are not mapped with OS threads thus these are not resource intensive
 *   just normal java objects in heap.
 * </p>
 * <p>
 *     Now lets see how virtual threads will solve the problems of platform threads which are mentioned above :
 *     <ul>
 *         <li>
 *             A new request is made to the server and a virtual thread is assigned the task.
 *         </li>
 *         <li>
 *             Now JVM will align this virtual thread to an OS level thread and the task will be progress in similar way it was done by traditional threads.
 *         </li>
 *         <li>
 *             Once there is any sort of blocking call say : Database call!
 *         </li>
 *         <li>
 *             As soon as the virtual thread goes to waiting state , the underlying OS thread will be free for other tasks. Now there is no need for the OS thread to wait till the response is available.
 *         </li>
 *         <li>
 *             When the response is available again an OS thread is assigned to the parked virtual thread and resumes the processing.
 *         </li>
 *     </ul>
 * </p>
 * <p>
 *     <b>
 *         Note : <code>Virtual</code> threads are best suited to executing code that spends most of its time blocked, waiting for data to arrive on a network socket or waiting for an element in queue for example.
 *     </b>
 * </p>
 *
 * @see <a href="https://openjdk.org/jeps/425">JEP:425 Virtual Threads</a> for detailed information.
 */

public class VirtualThreadsExample {

    private static final Runnable runnable = () -> System.out.println("This is a virtual thread: " + Thread.currentThread());

    public static void main(String[] args) throws InterruptedException {

        /**
         * Virtual thread creation using {@link Thread#ofVirtual()}
         */
        Thread virtualThread = Thread.ofVirtual().name("Virtual-Thread:1").start(runnable);

        /**
         * Virtual thread creation using {@link Thread#startVirtualThread(Runnable)}
         */
        Thread vThread = Thread.startVirtualThread(runnable);
        vThread.setName("Virtual-Thread:2");

        virtualThread.join();
        vThread.join();


        /**
         * Below is the output of the above line of code :
         * Here you can see the name of virtual thread as well as the name of platform thread on which these virtual threads got executed.
         * By Default JVM uses ForkJoinPool for platform threads which will act as carrier for the virtual threads.
         *
         * O/P :
         * This is a virtual thread: VirtualThread[#23,Virtual-Thread:2]/runnable@ForkJoinPool-1-worker-2
         * This is a virtual thread: VirtualThread[#21,Virtual-Thread:1]/runnable@ForkJoinPool-1-worker-1
         */
    }
}






















