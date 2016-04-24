package it.csbeng.androidbase.androidbase.core;

import android.content.Context;

import java.util.concurrent.BlockingQueue;

/**
 * @author Carmine Ingaldi
 * @version 0.0.1
 *
 * This abstraction is slightly different from {@link BaseRunnableCore} because implements a real
 * event loop. Prefer it when you have to process a stream of data in a different thread, saving the
 * overhead needed to create and destroy a new thread each time a new computation is done
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
