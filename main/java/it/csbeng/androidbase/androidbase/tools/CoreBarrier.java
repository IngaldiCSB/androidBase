package it.csbeng.androidbase.androidbase.tools;

import it.csbeng.androidbase.androidbase.core.BaseCore;

/**
 * @author Carmine Ingaldi
 * @version 0.0.1
 *
 * This brings a bunch of cores and execute them in parallel (or sequentally, if they are plain BaseCore, but without any guarantee about
 * the execution order) and waits until all of them are completed. Then executes a core passed as argument into all() method
 *
 */
public class CoreBarrier {

    public void all(BaseCore all)
    {
        throw new UnsupportedOperationException("implement All in CoreBarrier!!!!");
    }
}
