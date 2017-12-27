package net.tkjohn;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(2)
@Measurement(iterations = 5)
@Warmup(iterations = 5)
public class AddDecLockTestMain {
    private static final int PARALLEL_SIZE = 1000;
    private static final int TIMES_PER_THREAD = 500;

    private static final ExecutorService executorService = Executors.newFixedThreadPool(PARALLEL_SIZE);

    public static void main(String[] args) throws InterruptedException {
        AddDecLockTestMain testMain = new AddDecLockTestMain();
        testMain.setup();

        testMain.doubleSynchronizedLockerTest();
        testMain.singleSynchronizedLockerTest();
        testMain.reentrantLockLocker();

        testMain.tearDown();
    }

    @Setup
    public void setup() {
    }

    @TearDown
    public void tearDown() {
        executorService.shutdown();
    }

    @Benchmark
    public void doubleSynchronizedLockerTest() throws InterruptedException {
        lockerRunner(new DoubleSynchronizedLocker());
    }

    @Benchmark
    public void singleSynchronizedLockerTest() throws InterruptedException {
        lockerRunner(new SingleSynchronizedLocker());
    }

    @Benchmark
    public void reentrantLockLocker() throws InterruptedException {
        lockerRunner(new ReentrantLockLocker());
    }

    private void lockerRunner(ILocker locker) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(PARALLEL_SIZE);

        for (int i = 0; i < PARALLEL_SIZE; i++) {
            executorService.submit(new TestRunnable(i, locker, countDownLatch));
        }

        countDownLatch.await();
    }

    private static class TestRunnable implements Runnable {
        private final CountDownLatch countDownLatch;
        private final ILocker locker;
        private final boolean doingInc;

        TestRunnable(int id, ILocker locker, CountDownLatch countDownLatch) {
            this.locker = locker;
            this.countDownLatch = countDownLatch;
            this.doingInc = (id % 2 == 0);
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < TIMES_PER_THREAD; i++) {
                    if (doingInc) {
                        locker.inc();
                    } else {
                        locker.dec();
                    }
                }
            } catch (InterruptedException ignored) {
                //won't happen
            } finally {
                countDownLatch.countDown();
            }
        }
    }
}