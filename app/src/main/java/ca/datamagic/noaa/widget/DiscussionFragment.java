package ca.datamagic.noaa.widget;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.logging.Logger;

import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 1/10/2016.
 */
public class DiscussionFragment extends Fragment implements Renderer {
    private static Logger _logger = LogFactory.getLogger(DiscussionFragment.class);

    public String getDiscussion() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            return arguments.getString("discussion", null);
        }
        return null;
    }

    public void setDiscussion(String newVal) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            arguments.putString("discussion", newVal);
        }
    }

    public static DiscussionFragment newInstance() {
        return newInstance(null);
    }

    public static DiscussionFragment newInstance(String discussion) {
        DiscussionFragment fragment = new DiscussionFragment();
        Bundle bundle = new Bundle();
        bundle.putString("discussion", discussion);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discussion_main, container, false);
        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        _logger.info("onViewStateRestored");
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        _logger.info("onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void render() {
        View view = getView();
        if (view != null) {
            TextView discussionView = (TextView) view.findViewById(R.id.discussionView);
            String discussion = getDiscussion();
            if ((discussion != null) && (discussion.length() > 0)) {
                discussionView.setText(discussion);
            } else {
                discussionView.setText("Discussion not available");
            }
        }
    }

    @Override
    public void cleanup() {

    }
}
