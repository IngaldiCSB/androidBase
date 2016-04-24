package it.csbeng.androidbase.androidbase.core;

import android.content.Context;

import it.csbeng.androidbase.androidbase.tools.BaseFuture;

/**
 * @author Carmine Ingaldi
 * @version 0.0.1
 *
 * This abstraction provides an utility to implement cores that run in a different thread. Think about it like an
 * {@link android.os.AsyncTask} that returns something to the caller when notifyXXX() methods are called within
 */
public abstract class BaseRunnableCore<INPUT , OUTPUT , ERROR> extends BaseCore implements Runnable , IBaseCore
{
    private INPUT input;
    protected Thread mRunner = new Thread(this);

    public BaseRunnableCore(Context mContext) {
        super(mContext);
    }

    public BaseRunnableCore(Context context, IBaseListener listener) {
        super(context, listener);
    }

    public BaseRunnableCore(Context context, BaseCore decorated) {
        super(context, decorated);
    }

    public BaseRunnableCore(IBaseListener listener, Context context, BaseCore decorated) {
        super(listener, context, decorated);
    }

    @Override
    protected void notifyProgress(int progressLevel, int warningLevel) {
        super.notifyProgress(progressLevel, warningLevel);
    }

    @Override
    protected void notifyResult(Object o) {
        super.notifyResult((OUTPUT)o);
    }

    @Override
    protected void notifyError(Object o) {
        super.notifyError((ERROR)o);
    }

    @Override
    protected BaseFuture<?> next(Object o) {
        return super.next((INPUT)o);
    }

    @Override
    public  void run()
    {
        execute(input);
    }

    abstract void execute(Object o);

    @Override
    public void resolve(Object input)
    {
        this.input = (INPUT) input;
        mRunner.start();
    }

    @Override
    public void dispose()
    {
        try
        {
            mRunner.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
