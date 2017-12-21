package net.tkjohn;

import org.openjdk.jmh.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * LockTest
 *
 * @author fanzw
 * @since 2017/12/21
 */
@State(Scope.Benchmark)
public class AddDecLockTestMain {
	private static final Logger logger = LoggerFactory.getLogger(AddDecLockTestMain.class);

	private static final int parallelSize = 1000;
	private static final int times = 500;

	private static ExecutorService executorService;

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
		executorService = Executors.newFixedThreadPool(parallelSize);
	}

	@TearDown
	public void tearDown() {
		executorService.shutdown();
	}

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	@Fork(2)
	@Measurement(iterations = 5)
	@Warmup(iterations = 5)
	public void doubleSynchronizedLockerTest() throws InterruptedException {
		ILocker locker = new DoubleSynchronizedLocker();
		CountDownLatch countDownLatch = new CountDownLatch(parallelSize);

		for (int i = 0; i < parallelSize; i++) {
			executorService.submit(new TestRunnable(i, locker, countDownLatch));
		}

		countDownLatch.await();
	}

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	@Fork(2)
	@Measurement(iterations = 5)
	@Warmup(iterations = 5)
	public void singleSynchronizedLockerTest() throws InterruptedException {
		ILocker locker = new SingleSynchronizedLocker();
		CountDownLatch countDownLatch = new CountDownLatch(parallelSize);

		for (int i = 0; i < parallelSize; i++) {
			executorService.submit(new TestRunnable(i, locker, countDownLatch));
		}

		countDownLatch.await();
	}

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	@Fork(2)
	@Measurement(iterations = 5)
	@Warmup(iterations = 5)
	public void reentrantLockLocker() throws InterruptedException {
		ILocker locker = new ReentrantLockLocker();
		CountDownLatch countDownLatch = new CountDownLatch(parallelSize);

		for (int i = 0; i < parallelSize; i++) {
			executorService.submit(new TestRunnable(i, locker, countDownLatch));
		}

		countDownLatch.await();
	}


	private static class TestRunnable implements Runnable {
		private final CountDownLatch countDownLatch;
		private final int id;
		private final ILocker locker;

		TestRunnable(int id, ILocker locker, CountDownLatch countDownLatch) {
			this.id = id;
			this.locker = locker;
			this.countDownLatch = countDownLatch;
		}

		@Override
		public void run() {
			try {
				for (int i = 0; i < times; i++) {
					if (id % 2 == 0) {
						locker.inc();
					} else {
						locker.dec();
					}
					logger.debug("finished once");
				}
			} catch (InterruptedException ignored) {
			}
			countDownLatch.countDown();
		}
	}
}