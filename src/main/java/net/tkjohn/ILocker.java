package net.tkjohn;

/**
 * LockTest
 *
 * @author fanzw
 * @since 2017/12/21
 */
public interface ILocker {
	void inc() throws InterruptedException;

	void dec() throws InterruptedException;
}
