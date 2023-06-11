package ca.datamagic.noaa.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ca.datamagic.noaa.async.AccountingTask;
import ca.datamagic.noaa.dao.PreferencesDAO;
import ca.datamagic.noaa.dto.PreferencesDTO;

public class NewFeaturesDialog extends Dialog implements View.OnClickListener {
    private String _newFeatures = null;
    private TextView _newFeaturesView = null;
    private CheckBox _showNextTime = null;
    private Button _okButton = null;

    public NewFeaturesDialog(@NonNull Context context, String newFeatures) {
        super(context);
        _newFeatures = newFeatures;
    }

    public NewFeaturesDialog(@NonNull Context context, int themeResId, String newFeatures) {
        super(context, themeResId);
        _newFeatures = newFeatures;
    }

    protected NewFeaturesDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, String newFeatures) {
        super(context, cancelable, cancelListener);
        _newFeatures = newFeatures;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_features);
        setCancelable(false);
        setTitle(R.string.newFeatures);
        _newFeaturesView = findViewById(R.id.newFeatures);
        _newFeaturesView.setText(_newFeatures);
        _showNextTime = findViewById(R.id.showNextTime);
        _showNextTime.setChecked(true);
        _okButton = findViewById(R.id.ok);
        _okButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        boolean showNextTime = _showNextTime.isChecked();
        PreferencesDAO dao = new PreferencesDAO(getContext());
        PreferencesDTO dto = dao.read();
        dto.setShowNewFeatures(showNextTime);
        dao.write(dto);
        (new AccountingTask("NewFeatures", "Version")).execute();
        dismiss();
        if (MainActivity.getThisInstance() != null) {
            MainActivity.getThisInstance().actionRefresh();
        }
    }
}
