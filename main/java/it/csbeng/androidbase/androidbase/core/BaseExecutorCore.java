package it.csbeng.androidbase.androidbase.core;

import android.content.Context;

/**
 * Created by gennaro on 24/04/2016.
 */
public abstract class BaseExecutorCore <INPUT , OUTPUT , ERROR> extends BaseRunnableCore {

    public BaseExecutorCore(Context mContext) {
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
}
