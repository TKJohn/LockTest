package net.tkjohn;

public class DoubleSynchronizedLocker implements ILocker {
    private final Object incLocker = new Object();
    private final Object decLocker = new Object();
    private final Object operLocker = new Object();

    private int i = 0;

    @Override
    public void inc() throws InterruptedException {
        synchronized (incLocker) {
            synchronized (operLocker) {
                while (i > 0) {
                    operLocker.wait();
                }
                i++;
                operLocker.notify();
            }
        }
    }

    @Override
    public void dec() throws InterruptedException {
        synchronized (decLocker) {
            synchronized (operLocker) {
                while (i < 1) {
                    operLocker.wait();
                }
                i--;
                operLocker.notify();
            }
        }
    }
}