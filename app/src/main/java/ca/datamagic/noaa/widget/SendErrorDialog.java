package ca.datamagic.noaa.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import ca.datamagic.noaa.async.AsyncTaskListener;
import ca.datamagic.noaa.async.AsyncTaskResult;
import ca.datamagic.noaa.async.SendErrorTask;

public class SendErrorDialog extends Dialog implements View.OnClickListener {
    private EditText _errorInformation = null;
    private Button _okButton = null;
    private Button _cancelButton = null;
    private ProgressBar _sendErrorProgress = null;
    private boolean _processing = false;

    public SendErrorDialog(@NonNull Context context) {
        super(context);
    }

    public SendErrorDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected SendErrorDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        if (window != null) {
            Rect displayRectangle = new Rect();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            View layout = getLayoutInflater().inflate(R.layout.senderror_main, null);
            layout.setMinimumWidth((int)(displayRectangle.width() * 0.9f));
            layout.setMinimumHeight((int)(displayRectangle.height() * 0.9f));
            setContentView(layout);
        } else {
            setContentView(R.layout.senderror_main);
        }
        setCancelable(false);
        setTitle(R.string.send_error_title);
        _errorInformation = (EditText)findViewById(R.id.errorInformation);
        _okButton = (Button)findViewById(R.id.sendErrorOK);
        _cancelButton = (Button)findViewById(R.id.sendErrorCancel);
        _sendErrorProgress = (ProgressBar)findViewById(R.id.sendErrorProgress);
        _errorInformation.setText("");
        _okButton.setOnClickListener(this);
        _cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendErrorOK:
                sendError();
                break;
            case R.id.sendErrorCancel:
                dismiss();
                break;
        }
    }

    private void sendError() {
        if (_processing) {
            return;
        }
        try {
            _processing = true;
            _sendErrorProgress.setVisibility(View.VISIBLE);
            String errorInformation = _errorInformation.getText().toString();
            String logPath = MainActivity.getThisInstance().getFilesPath();
            String mailFrom = "no-reply@datamagic.ca";
            String mailSubject = "NOAAWeatherWidget Error";
            StringBuffer mailBody = new StringBuffer();
            mailBody.append(errorInformation);
            mailBody.append("\n");
            SendErrorTask task = new SendErrorTask(logPath, mailFrom, mailSubject, mailBody.toString());
            task.addListener(new AsyncTaskListener<Void>() {
                @Override
                public void completed(AsyncTaskResult<Void> result) {
                    _processing = false;
                    _sendErrorProgress.setVisibility(View.GONE);
                    dismiss();
                }
            });
            task.execute((Void[]) null);
        } catch (Throwable t) {
            // TODO: Show error
            _processing = false;
            _sendErrorProgress.setVisibility(View.GONE);
        }
    }
}
