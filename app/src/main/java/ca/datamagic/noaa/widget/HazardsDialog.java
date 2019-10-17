package ca.datamagic.noaa.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.List;

import ca.datamagic.noaa.async.AccountingTask;

public class HazardsDialog extends Dialog {
    public HazardsDialog(@NonNull Context context) {
        super(context);
    }

    public HazardsDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected HazardsDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        if (window != null) {
            Rect displayRectangle = new Rect();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            View layout = getLayoutInflater().inflate(R.layout.hazards_main, null);
            layout.setMinimumWidth((int) (displayRectangle.width() * 0.9f));
            layout.setMinimumHeight((int) (displayRectangle.height() * 0.9f));
            setContentView(layout);
        } else {
            setContentView(R.layout.hazards_main);
        }
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setTitle(R.string.hazards_page_title);
        TextView hazardsView = (TextView)findViewById(R.id.hazardsView);
        StringBuilder builder = new StringBuilder();
        builder.append(getContext().getResources().getString(R.string.hazardsInstructions));
        List<String> hazards = MainActivity.getThisInstance().getHazards();
        for (int ii = 0; ii < hazards.size(); ii++) {
            builder.append("\n");
            builder.append(hazards.get(ii));
        }
        hazardsView.setText(builder.toString());
        (new AccountingTask("Hazards", "Show")).execute((Void[])null);
    }
}
