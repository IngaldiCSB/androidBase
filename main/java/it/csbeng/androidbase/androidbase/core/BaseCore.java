package it.csbeng.androidbase.androidbase.core;

import android.content.Context;

import it.csbeng.androidbase.tools.BaseFuture;
import java.util.concurrent.Future;

/**
 * @author Carmine Ingaldi
 * @version 0.0.1
 *
 * <p></p>A core, in the Base architecture concept, is a single step of a complex use case.
 * The core processes an input (of course, of generic type {@code INPUT} coming from different sources,
 * the only constraint is that the event source rmust provide a {@link android.content.Context}.
 * Activities, Services or BroadcastReceivers are the perfect candidates to manage cores; actually, an
 * {@link android.app.Application} too</p>
 *
 * <p>Extend {@link BaseCore} class to get your own cores, then compose them like lego bricks to build
 * complex interaction among several etherogeneous components, then provide them proper listeners capable to
 * react to different results coming out from core execution</p>
 */
public abstract class BaseCore<INPUT , OUTPUT , ERROR>
{
    private IBaseListener mListener = new NILListener();
    private Context mContext = null;

    private BaseCore<INPUT , OUTPUT , ERROR> decorated = null;
    private BaseCore<INPUT , OUTPUT , ERROR> decorator = null;

    private boolean resultProduced = false;

    public BaseCore(Context mContext)
    {
        this.mContext = mContext;
    }

    public BaseCore(Context context, IBaseListener listener)
    {
        this(context);
        this.mListener = listener;
    }

    public BaseCore(Context context, BaseCore<INPUT , OUTPUT , ERROR> decorated)
    {
        this(context);
        this.decorated = decorated;
    }

    public BaseCore(IBaseListener listener, Context context, BaseCore<INPUT , OUTPUT , ERROR> decorated)
    {
        this(context, listener);
        this.decorated = decorated;
    }

    protected void notifyProgress (int progressLevel , int warningLevel)
    {
        mListener.onProgress(mContext , progressLevel , warningLevel);
    }

    protected void notifyResult(OUTPUT output)
    {
        if (!resultProduced)
        {
            mListener.onResult(mContext , output);
            resultProduced = true;
        }

        if(decorator != null)
        {

        }

    }

    protected void notifyError(ERROR error)
    {
        mListener.onError(mContext , error);
    }


    protected BaseFuture<?> next(INPUT input)
    {
        if (decorated != null)
        {
            decorated.execute(input);
        }

        return new BaseFuture<OUTPUT>(decorated);
    }

    /**
     *
     * @param input
     */
    abstract void execute (INPUT input );

    public  void resolve (INPUT input)
    {
        execute(input);
    }

    public void dispose ()
    {
        //Nothing to to here!;
    }


    interface IBaseListener <OUTPUT , ERROR>
    {
        /**
         *Implement logic that reacts to progress events emitted from your core.
         * Maybe you need a DownloadFileCore attached to a dialog that displays the amount
         * of data dowloaded in KBs: notify periodically a progress event from your core logic
         * and pass the amount of data as progress level. Do you want to diplay some harmful condition?
         * increment the warning level and emit a progress event!
         *
         * @param context coming from the component that has instantiated the associated context
         * @param progressLevel a generic integer value that signals the progress state of the execution
         * @param warningLevel a generic integer value that signals the ste of guard. You can use 0 if everything is fine
         *                     1 to display a warning dialog and so on...
         */
        void onProgress(Context context , int progressLevel , int warningLevel);

        /**
         *
         * @param context coming from the component that has instantiated the associated context
         * @param outputData Data that passes the result of computation
         */
        void onResult (Context context , OUTPUT outputData);

        /**
         *
         * @param context coming from the component that has instantiated the associated context
         * @param errorData data that signals an error condition. Here I suggest to use enums to enumerate
         *                  error conditions that the listener implementor could face
         */
         void onError (Context context  , ERROR errorData);

    }

}

/**
 * just to have a listener that does nothing. In some cases you would like to have a core
 * that doesn't emit events, just plain data processing. So this is its default behaviour
 */
class NILListener  implements BaseCore.IBaseListener <Void , Void>
{

    @Override
    public void onProgress(Context context, int progressLevel, int warningLevel) {

    }

    @Override
    public void onResult(Context context, Void outputData) {

    }

    @Override
    public void onError(Context context, Void errorData) {

    }
}