package ca.datamagic.noaa.widget;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Greg on 1/10/2016.
 */
public class DiscussionFragment extends Fragment implements Renderer {
    private String _discussion = null;

    public void setDiscussion(String newVal) {
        _discussion = newVal;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discussion_main, container, false);
        TextView discussionView = (TextView)view.findViewById(R.id.discussionView);
        if ((_discussion != null) && (_discussion.length() > 0)) {
            discussionView.setText(_discussion);
        } else {
            discussionView.setText("Discussion not available");
        }
        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        try {
            String discussion = savedInstanceState.getString("discussion");
            _discussion = discussion;
        } catch (NullPointerException ex) {
            // Do Nothing
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("discussion", ((_discussion == null) ? "" : _discussion));
    }

    @Override
    public void render() {
        View view = getView();
        if (view != null) {
            TextView discussionView = (TextView) view.findViewById(R.id.discussionView);
            if ((_discussion != null) && (_discussion.length() > 0)) {
                discussionView.setText(_discussion);
            } else {
                discussionView.setText("Discussion not available");
            }
        }
    }
}
