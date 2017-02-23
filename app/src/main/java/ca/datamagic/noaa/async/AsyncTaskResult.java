package ca.datamagic.noaa.async;

/**
 * Created by Greg on 12/31/2015.
 */
public class AsyncTaskResult<T> {
    private T _result = null;
    private Throwable _throwable = null;

    public AsyncTaskResult() {
        super();
    }

    public AsyncTaskResult(T result) {
        super();
        _result = result;
    }

    public AsyncTaskResult(Throwable throwable) {
        super();
        _throwable = throwable;
    }

    public T getResult() {
        return _result;
    }

    public Throwable getThrowable() {
        return _throwable;
    }
}
