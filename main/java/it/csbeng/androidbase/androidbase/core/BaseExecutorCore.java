package it.csbeng.androidbase.androidbase.core;

import android.content.Context;

import java.util.concurrent.BlockingQueue;

/**
 * Created by gennaro on 24/04/2016.
 */
public abstract class BaseExecutorCore <INPUT , OUTPUT , ERROR> extends BaseRunnableCore
{
    private BlockingQueue<INPUT> inputs;

    private boolean isStarted = false;

    public BaseExecutorCore(Context mContext)
    {
        super(mContext);
    }

    public BaseExecutorCore(Context context, IBaseListener listener) {
        super(context, listener);
    }

    public BaseExecutorCore(Context context, BaseCore decorated) {
        super(context, decorated);
    }

    public BaseExecutorCore(IBaseListener listener, Context context, BaseCore decorated) {
        super(listener, context, decorated);
    }

    @Override
    abstract void execute(Object o);

    @Override
    public void resolve(Object input)
    {
        if (!isStarted)
        {
            isStarted = true;
            mRunner.start();
        }

        inputs.offer((INPUT)input);


    }

    @Override
    public void run()
    {

        while (isStarted)
        {
            try
            {
                execute(inputs.take());
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

    }
}
