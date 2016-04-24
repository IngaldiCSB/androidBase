package it.csbeng.androidbase.androidbase.tools;

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
public class BaseFuture<T> implements Future<T>
{
    private BaseCore mProducer = null;
    private T mValue = null;
    private CountDownLatch mSemaphore = new CountDownLatch(1);

    public BaseFuture(BaseCore producer)
    {
        this.mProducer = producer;
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

    public void set(T value)
    {
        mValue = value;
        mSemaphore.countDown();
    }
}
