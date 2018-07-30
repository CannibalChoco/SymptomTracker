package com.example.user.symptomtracker.ui.DialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.database.entity.TreatmentEntity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddPastTreatmentDialog extends DialogFragment {

    public static final int TIME_UNIT_NOT_SELECTED = -1;
    private static final int TIME_UNIT_HOUR = 0;
    private static final int TIME_UNIT_DAY = 1;
    private static final int TIME_UNIT_WEEK = 2;
    private static final int TIME_UNIT_MONTH = 3;

    private static final int WEEK_IN_DAYS = 7;
    private static final int MONTH_IN_DAYS = 30;

    @BindView(R.id.editCurrentTreatmentName)
    EditText editTreatment;
    @BindView(R.id.editCurrentTime)
    EditText editTime;
    @BindView(R.id.radioTimeHour)
    RadioButton timeHour;
    @BindView(R.id.radioTimeDay)
    RadioButton timeDay;
    @BindView(R.id.radioTimeWeek)
    RadioButton timeWeek;
    @BindView(R.id.radioTimeMonth)
    RadioButton timeMonth;

    @BindView(R.id.radioTreatmentSuccessful)
    RadioButton treatmentSuccessful;
    @BindView(R.id.radioTreatmentUnsuccessful)
    RadioButton treatmentUnsuccessful;

    private int selectedTimeUnit;
    private int treatmentSuccessInt;

    public interface OnSavePastTreatment {
        void onSavePastTreatment(String name, long takesEffectIn, int wasSuccessful);
    }

    private OnSavePastTreatment listener;

    public void setOnSavePastTreatmentListener(OnSavePastTreatment listener) {
        this.listener = listener;
    }

    public AddPastTreatmentDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        selectedTimeUnit = -1;
        treatmentSuccessInt = TreatmentEntity.WAS_SUCCESSFUL_NOT_SET;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.add_past_treatment_title)
                .setPositiveButton(R.string.action_save, (dialog, which) -> saveSymptom(dialog))
                .setNegativeButton(R.string.action_cancel, (dialog, which) -> dialog.dismiss());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_past_treatment, null);
        ButterKnife.bind(this, view);

        dialogBuilder.setView(view);
        return dialogBuilder.create();
    }

    /**
     * Extract data entered in EditText fields, send it via listener
     * @param dialog
     */
    private void saveSymptom(DialogInterface dialog) {
        String timeString = editTime.getText().toString();
        String treatment = editTreatment.getText().toString();

        if (!timeString.isEmpty()
                && selectedTimeUnit != TIME_UNIT_NOT_SELECTED
                && !treatment.isEmpty()){
            // all data is provided, send treatments name and estimated time to take effect
            int time = Integer.valueOf(timeString);
            long timeInMillis = getTimeInMillis(time);
            listener.onSavePastTreatment(treatment, timeInMillis, treatmentSuccessInt);
        } else if ((timeString.isEmpty()
                && selectedTimeUnit == TIME_UNIT_NOT_SELECTED)
                && !treatment.isEmpty()){
            // only name is provided
            listener.onSavePastTreatment(treatment, TreatmentEntity.TIME_NOT_SELECTED,
                    treatmentSuccessInt);
        } else if ((timeString.isEmpty()
                || selectedTimeUnit == TIME_UNIT_NOT_SELECTED)
                && treatment.isEmpty()){
            // TODO: Don't exit dialog
            Toast.makeText(getContext(), "Please fill all necessary fields", Toast.LENGTH_SHORT).show();
        } else {
            dialog.dismiss();
        }
    }

    @OnClick({R.id.radioTimeHour, R.id.radioTimeDay, R.id.radioTimeWeek, R.id.radioTimeMonth})
    public void timeUnitSelected() {
        if (timeHour.isChecked()) {
            selectedTimeUnit = TIME_UNIT_HOUR;
        } else if (timeDay.isChecked()) {
            selectedTimeUnit = TIME_UNIT_DAY;
        } else if (timeWeek.isChecked()) {
            selectedTimeUnit = TIME_UNIT_WEEK;
        } else if (timeMonth.isChecked()) {
            selectedTimeUnit = TIME_UNIT_MONTH;
        }
    }

    @OnClick({R.id.radioTreatmentSuccessful, R.id.radioTreatmentUnsuccessful})
    public void successSelected(){
        if(treatmentSuccessful.isChecked()){
            treatmentSuccessInt = TreatmentEntity.WAS_SUCCESSFUL_YES;
        } else if (treatmentUnsuccessful.isChecked()){
            treatmentSuccessInt = TreatmentEntity.WAS_SUCCESSFUL_NO;
        }
    }

    /**
     * Calculate time in miliseconds from the data user has entered in dialog
     * @param time count of time units
     * @return time in miliseconds
     */
    private long getTimeInMillis(int time) {
        if (selectedTimeUnit != TIME_UNIT_NOT_SELECTED){
            switch (selectedTimeUnit){
                case TIME_UNIT_HOUR:
                    return TimeUnit.HOURS.toMillis(time);
                case TIME_UNIT_DAY:
                    return TimeUnit.DAYS.toMillis(time);
                case TIME_UNIT_WEEK:
                    return TimeUnit.DAYS.toMillis(time) * WEEK_IN_DAYS;
                case TIME_UNIT_MONTH:
                    return TimeUnit.DAYS.toMillis(time) * MONTH_IN_DAYS;
            }
        }
        return 0;
    }
}
