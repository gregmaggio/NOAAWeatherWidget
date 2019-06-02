package ca.datamagic.noaa.widget;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.logging.Logger;

import ca.datamagic.noaa.async.AccountingTask;
import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 1/10/2016.
 */
public class DiscussionFragment extends Fragment implements Renderer {
    private static Logger _logger = LogFactory.getLogger(DiscussionFragment.class);

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
        View view = getView();
        if (view != null) {
            String discussion = null;
            MainActivity mainActivity = MainActivity.getThisInstance();
            if (mainActivity != null) {
                discussion = mainActivity.getDiscussion();
            }
            TextView discussionView = (TextView) view.findViewById(R.id.discussionView);
            if ((discussion != null) && (discussion.length() > 0)) {
                discussionView.setText(discussion);
            } else {
                discussionView.setText("Discussion not available");
            }
        }
        (new AccountingTask("Discussion", "Render")).execute((Void[])null);
    }

    @Override
    public void cleanup() {

    }
}
