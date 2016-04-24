package it.csbeng.androidbase.androidbase.core;

import android.content.Context;

import it.csbeng.androidbase.tools.BaseFuture;
import java.util.concurrent.Future;

/**
 * @author Carmine Ingaldi
 * @version 0.0.1
 *
 * <p>A core, in the Base architecture concept, is a single step of a complex use case.
 * The core processes an input (of course, of generic type {@code INPUT} coming from different sources,
 * the only constraint is that the event source must provide a {@link android.content.Context}.
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

    private boolean resultProduced = false;
    private Future<OUTPUT> result = new BaseFuture<>(this);

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

    public Future<OUTPUT> getResult()
    {
        return result;
    }

    protected void notifyProgress (int progressLevel , int warningLevel)
    {
        mListener.onProgress(mContext , progressLevel , warningLevel);
    }

    /**
     * Wraps a call that catches an event which says: "hey, this work is done and everything is fine!"
     * Registered listener, if any exists, will be notified and the future value is resolved, so anyone
     * that receives the value, can consume it. It does not make sense to call this method twice or more during
     * a single core execution, so if a scatterbrain developer does it, the subsequent calls have no effects
     *
     * @param output the data outputted when the core has finished correctly its work
     */
    protected void notifyResult(OUTPUT output)
    {
        if (!resultProduced)
        {
            mListener.onResult(mContext , output);
            ((BaseFuture) result).set(output);

            resultProduced = true;
        }

    }

    protected void notifyError(ERROR error)
    {
        mListener.onError(mContext , error);
    }


    /**
     * executes the next core in the decoration chain. This version admits just cores with same input/output type parameters
     * Remember that you may need to call this method when you add a decorated core and want to executed in the middle (or, likely, at the end)
     * of the decorating core logic, in a synchronous, time-blocking fashion; just like the decorated {@code #execute()} code
     * would be written into decorating code one.
     * An issue emerging from this usage pattern is that you don't know (or, at least, pretend to not knowing) if the decorated core
     * is actually execute sequentially, or in a different thread (so in parallel). You may be interested in processing the output
     * from decorated core or maybe not, but if you are you must wait for it, if not immediately avaliable.
     * So, this method provides a result and provides it with a {@see Future}, resolved when the decorated core has a result to give
     * Notice that the result could be null: the decorated core does not provide an output or maybe the execution ha encountered an error,
     * so before to process the result, is better that you check for null-ity
     * @param input
     * @return
     */
    protected BaseFuture<?> next(INPUT input)
    {
        BaseFuture<OUTPUT> f = null;
        if (decorated != null)
        {
            decorated.execute(input);
            f = (BaseFuture)decorated.getResult();

        }

        return f;
    }


    /**
     *This is the most important method of the BaseCore class. Here you have to put your own logic, interact with other cores and
     * emit events. Keep in mind that cores are meant to be disposable, so you should not execute it twice in the same context (but I admit that
     * some exception could be reasonable)
     * @param input
     */
    abstract void execute (INPUT input );

    /**
     * Causes the core exectution
     * @param input
     */
    public  void resolve (INPUT input)
    {
        this.resultProduced = false;
        execute(input);
    }

     /*
     *  This method provides a different way to compose cores together. Respect to decoration, call to this method causes the execution
     *  of the core passed as argument just after the execution of the core on which the then() method was called. Thus, you can create a
     *  chain of execution.
     *  Notice that it does not make sense to follow that chain if a core issues an error event in the middle of them, so the core passed
     *  in then()'s argument will be exectued only after that the preceiding core has issued the onResult() method
     * @param thenCore
     */
    public void then (BaseCore thenCore)
    {
        throw new UnsupportedOperationException("implement then method in BaseCore!!!");
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