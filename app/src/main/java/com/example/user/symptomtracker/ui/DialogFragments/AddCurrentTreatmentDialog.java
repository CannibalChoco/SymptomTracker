package com.example.user.symptomtracker.ui.DialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.example.user.symptomtracker.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddCurrentTreatmentDialog extends DialogFragment{

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

    private long selectedTimeUnit;

    public interface OnSaveCurrentTreatment{
        void onSaveCurrentTreatment(String name, long takesEffectIn);
    }

    private OnSaveCurrentTreatment listener;

    public void setOnSaveCurrentTreatmentListener(OnSaveCurrentTreatment listener){
        this.listener = listener;
    }

    public AddCurrentTreatmentDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.add_current_treatment_title)
                .setPositiveButton(R.string.dialog_action_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String treatment = editTreatment.getText().toString();
                        String time = editTime.getText().toString();
                        if (!treatment.isEmpty() && !time.isEmpty()){
                            // TODO: send data through listener
                            listener.onSaveCurrentTreatment(treatment, selectedTimeUnit);
                        } else {
                            dialog.dismiss();
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_action_discard, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_current_treatment_dialog, null);
        ButterKnife.bind(this, view);

        dialogBuilder.setView(view);
        return dialogBuilder.create();
    }

    @OnClick({R.id.radioTimeHour, R.id.radioTimeDay, R.id.radioTimeWeek, R.id.radioTimeMonth})
    public void timeUnitSelected(){
        int time = Integer.valueOf(editTime.getText().toString());

        if (!editTime.getText().toString().isEmpty()){
            if (timeHour.isChecked()){
                selectedTimeUnit = TimeUnit.HOURS.toMillis(time);
            } else if (timeDay.isChecked()){
                selectedTimeUnit = TimeUnit.DAYS.toMillis(time);
            } else if (timeWeek.isChecked()){
                selectedTimeUnit = TimeUnit.DAYS.toMillis(time) * 7;
            } else if (timeMonth.isChecked()){
                selectedTimeUnit = TimeUnit.DAYS.toMillis(time) * 30;
            }
        }
    }
}
