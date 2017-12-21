package net.tkjohn;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * LockTest
 *
 * @author fanzw
 * @since 2017/12/21
 */
public class ReentrantLockLocker implements ILocker {
	private final Lock lock = new ReentrantLock();
	private final Condition canInc = lock.newCondition();
	private final Condition canDec = lock.newCondition();

	private int i = 0;

	@Override
	public void inc() throws InterruptedException {
		lock.lock();
		try {
			while (i > 0) {
				canInc.await();
			}
			i++;
			canDec.signalAll();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void dec() throws InterruptedException {
		lock.lock();
		try {
			while (i < 1) {
				canDec.await();
			}
			i--;
			canInc.signalAll();
		} finally {
			lock.unlock();
		}
	}
}