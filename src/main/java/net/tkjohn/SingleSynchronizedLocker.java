package net.tkjohn;

public class SingleSynchronizedLocker implements ILocker {
    private int i = 0;

    @Override
    public synchronized void inc() throws InterruptedException {
        while (i > 0) {
            wait();
        }
        i++;
        notifyAll();
    }

    @Override
    public synchronized void dec() throws InterruptedException {
        while (i < 1) {
            wait();
        }
        i--;
        notifyAll();
    }
}