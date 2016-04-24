package it.csbeng.androidbase.tools;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import it.csbeng.androidbase.androidbase.core.BaseCore;

/**
 * @author Carmine Ingaldi
 * @version 0.0.1
 */
public class BaseFuture<T> implements Future<T>
{
    private BaseCore called;

    public BaseFuture(BaseCore called)
    {
        this.called = called;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }
}
