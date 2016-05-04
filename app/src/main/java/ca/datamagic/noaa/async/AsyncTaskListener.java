package ca.datamagic.noaa.async;

/**
 * Created by Greg on 1/9/2016.
 */
public interface AsyncTaskListener<T> {
    public void Completed(AsyncTaskResult<T> result);
}
