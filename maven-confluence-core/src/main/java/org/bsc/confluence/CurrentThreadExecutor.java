package org.bsc.confluence;

import java.util.concurrent.Executor;

public class CurrentThreadExecutor implements Executor {
    public void execute(Runnable r) {
        r.run();
    }
    
    public final static CurrentThreadExecutor instance = new CurrentThreadExecutor();
}