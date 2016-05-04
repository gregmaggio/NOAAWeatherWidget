package ca.datamagic.noaa.widget;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.text.MessageFormat;

import ca.datamagic.noaa.dto.WFODTO;

/**
 * Created by Greg on 1/10/2016.
 */
public class DiscussionFragment extends Fragment {
    private ScrollView _discussionScroller = null;
    private WebView _discussionView = null;
    private WFODTO _wfo = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discussion_main, container, false);
        _discussionScroller = (ScrollView)view.findViewById(R.id.discussionScroller);
        _discussionView = (WebView)view.findViewById(R.id.discussionView);
        render(_wfo);
        return view;
    }

    public void render(WFODTO wfo) {
        _wfo = wfo;
        if (_wfo != null) {
            _discussionView.loadUrl(MessageFormat.format("http://forecast.weather.gov/product.php?site={0}&issuedby={0}&product=AFD&format=txt&version=1&glossary=1", _wfo.getWFO()));
        } else {
            _discussionView.loadData("", null, null);
        }
    }
}
