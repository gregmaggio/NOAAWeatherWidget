package ca.datamagic.noaa.widget;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.logging.Level;
import java.util.logging.Logger;

import androidx.fragment.app.Fragment;
import ca.datamagic.noaa.async.AccountingTask;
import ca.datamagic.noaa.async.AsyncTaskListener;
import ca.datamagic.noaa.async.AsyncTaskResult;
import ca.datamagic.noaa.async.DiscussionTask;
import ca.datamagic.noaa.async.RenderTask;
import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 1/10/2016.
 */
public class DiscussionFragment extends Fragment implements Renderer, NonSwipeableFragment {
    private static Logger _logger = LogFactory.getLogger(DiscussionFragment.class);
    private String _discussion = null;

    public static DiscussionFragment newInstance() {
        DiscussionFragment fragment = new DiscussionFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discussion_main, container, false);
        return view;
    }

    @Override
    public void render() {
        try {
            if (!MainActivity.getThisInstance().isFragmentActive(this)) {
                return;
            }
            View view = getView();
            if (view == null) {
                RenderTask renderTask = new RenderTask(this);
                renderTask.execute();
            } else {
                if (_discussion != null) {
                    TextView discussionView = view.findViewById(R.id.discussionView);
                    if (_discussion.length() > 0) {
                        discussionView.setText(_discussion);
                    } else {
                        discussionView.setText("Discussion not available");
                    }
                } else {
                    DiscussionTask task = new DiscussionTask();
                    task.addListener(new DiscussionListener());
                    task.execute();
                    MainActivity.getThisInstance().startBusy();
                }
            }
            (new AccountingTask("Discussion", "Render")).execute();
        } catch (IllegalStateException ex) {
            _logger.warning("IllegalStateException: " + ex.getMessage());
            RenderTask renderTask = new RenderTask(this);
            renderTask.execute();
        }
    }

    @Override
    public void cleanup() {
        _discussion = null;
    }

    @Override
    public boolean canSwipe(float x, float y) {
        return true;
    }

    private class DiscussionListener implements AsyncTaskListener<String> {
        @Override
        public void completed(AsyncTaskResult<String> result) {
            MainActivity.getThisInstance().stopBusy();
            if (result.getThrowable() != null) {
                if (_logger != null) {
                    _logger.log(Level.WARNING, "Error retrieving discussion.", result.getThrowable());
                }
                _discussion = null;
            } else {
                _discussion = result.getResult();
                if (_discussion == null) {
                    _discussion = "";
                }
            }
            View view = getView();
            if (view != null) {
                TextView discussionView = view.findViewById(R.id.discussionView);
                if ((_discussion != null) && (_discussion.length() > 0)) {
                    discussionView.setText(_discussion);
                } else {
                    discussionView.setText("Discussion not available");
                }
            }
        }
    }
}
