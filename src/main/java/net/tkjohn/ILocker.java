package net.tkjohn;

public interface ILocker {
    void inc() throws InterruptedException;

    void dec() throws InterruptedException;
}