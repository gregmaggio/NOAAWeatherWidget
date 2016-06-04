package ca.datamagic.noaa.widget;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.MessageFormat;

import ca.datamagic.noaa.dto.WFODTO;

/**
 * Created by Greg on 1/10/2016.
 */
public class DiscussionFragment extends Fragment {
    private ScrollView _discussionScroller = null;
    private TextView _discussionView = null;
    private String _discussion = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discussion_main, container, false);
        _discussionScroller = (ScrollView)view.findViewById(R.id.discussionScroller);
        _discussionView = (TextView)view.findViewById(R.id.discussionView);
        render(_discussion);
        return view;
    }

    public void render(String discussion) {
        _discussion = discussion;
        if (_discussionView != null) {
            if (_discussion != null) {
                _discussionView.setText(_discussion);
            } else {
                _discussionView.setText("Discussion not available");
            }
        }
    }
}
