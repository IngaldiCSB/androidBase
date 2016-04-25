package it.csbeng.androidbase.androidbase.tools;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import it.csbeng.androidbase.androidbase.core.BaseCore;

/**
 * @author Carmine Ingaldi
 * @version 0.0.1
 *
 * @// TODO: 24/04/2016 future should not expose a "set" method. Only the future owner should resolve its value
 *
 */
public class BaseFuture<T> implements Future<T>, Observer
{
    private BaseCore mProducer = null;
    private T mValue = null;
    private CountDownLatch mSemaphore = new CountDownLatch(1);

    public BaseFuture(BaseCore producer)
    {
        this.mProducer = producer;
        producer.addObserver(this);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning)
    {
        throw new UnsupportedOperationException("implement BaseFuture");
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException("implement BaseFuture");
    }

    @Override
    public boolean isDone() {
        throw new UnsupportedOperationException("implement BaseFuture");
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {

        mSemaphore.await();
        return mValue;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        throw new UnsupportedOperationException("implement BaseFuture");
    }

    protected void set(T value)
    {

    }

    @Override
    public void update(Observable observable, Object data)
    {
        mValue = (T) data;
        mSemaphore.countDown();
    }

    /**
     * Utility method that you can use to compare tow futures. Since their values should be resolved in some point of time and you
     * would not wait for that moment, this method wraps the result of comparision into a brand new future to be resolved later
     * @param a
     * @param b
     * @param mode
     * @return
     */
    public static BaseFuture<Boolean> compare (Future<? extends Comparable> a , Future<? extends Comparable> b , BaseFuture.CompareMode mode)
    {
        throw new UnsupportedOperationException("implement compare in BaseFuture!!!");
    }

    public enum CompareMode
    {
        EQ,
        UNEQ,
        GR,
        LE,
        GEQ,
        LEQ,
    }
}
