package com.example.user.symptomtracker.ui.DialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.database.entity.TreatmentEntity;
import com.example.user.symptomtracker.ui.TreatmentFragment;
import com.example.user.symptomtracker.utils.TimeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.user.symptomtracker.utils.TimeUtils.TIME_UNIT_DAY;
import static com.example.user.symptomtracker.utils.TimeUtils.TIME_UNIT_HOUR;
import static com.example.user.symptomtracker.utils.TimeUtils.TIME_UNIT_MONTH;
import static com.example.user.symptomtracker.utils.TimeUtils.TIME_UNIT_NOT_SELECTED;
import static com.example.user.symptomtracker.utils.TimeUtils.TIME_UNIT_WEEK;
import static com.example.user.symptomtracker.utils.TimeUtils.getTimeInMillis;

public class EditTreatmentDialog extends DialogFragment {

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
    @BindView(R.id.radioTreatmentCurrent)
    RadioButton radioCurrent;
    @BindView(R.id.radioTreatmentPast)
    RadioButton radioPast;

    @BindView(R.id.radioGroupTime)
    RadioGroup radioGroupTime;
    @BindView(R.id.radioGroupSuccess)
    RadioGroup radioGroupSuccess;
    @BindView(R.id.radioGroupIsActive)
    RadioGroup radioIsActive;

    @BindView(R.id.radioTreatmentSuccessful)
    RadioButton treatmentSuccessful;
    @BindView(R.id.radioTreatmentUnsuccessful)
    RadioButton treatmentUnsuccessful;

    private int selectedTimeUnit;
    private int treatmentSuccessInt;
    private int fragmentId;
    private int actionSaveOrEdit;
    private TreatmentEntity treatment;
    private int symptomId;

    public interface OnSaveTreatment {
        void onSaveTreatment(TreatmentEntity treatment, int actionSaveOrEdit);
    }

    private OnSaveTreatment listener;

    public void setOnSaveTreatmentListener(OnSaveTreatment listener) {
        this.listener = listener;
    }

    public EditTreatmentDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.add_past_treatment_title)
                .setPositiveButton(R.string.action_save, (dialog, which) -> saveSymptom(dialog))
                .setNegativeButton(R.string.action_cancel, (dialog, which) -> dialog.dismiss());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_treatment, null);
        ButterKnife.bind(this, view);

        dialogBuilder.setView(view);
        return dialogBuilder.create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        fragmentId = bundle.getInt(TreatmentFragment.KEY_FRAGMENT_ID);
        symptomId = bundle.getInt(TreatmentFragment.KEY_SYMPTOM_ID);
        actionSaveOrEdit = bundle.getInt(TreatmentFragment.KEY_ACTION_SAVE_OR_EDIT);

        // populate views if editing existing treatment
        if (actionSaveOrEdit == TreatmentFragment.ID_ACTION_EDIT){
            treatment = bundle.getParcelable(TreatmentFragment.KEY_TREATMENT);
            editTreatment.setText(treatment.getName());

            long takesEffectIn = treatment.getTakesEffectIn();
            if (takesEffectIn != TreatmentEntity.TIME_NOT_SELECTED){
                int timeUnit = TimeUtils.getTimeUnitForMillis(takesEffectIn);
                editTime.setText(String.valueOf(timeUnit));
                radioGroupTime.check(TimeUtils.getRadioButtonIdForTimestamp(takesEffectIn));
            } else {
                selectedTimeUnit = -1;
            }

            setRadioSuccessId(treatment.getWasSuccessful());
            setRadioIsActiveId(treatment.getIsActive());
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * Extract data entered in EditText fields, send it via listener
     * @param dialog
     */
    private void saveSymptom(DialogInterface dialog) {
        String timeString = editTime.getText().toString();
        String name = editTreatment.getText().toString();

        long timeInMillis;
        boolean isActive;

        if (radioCurrent.isChecked()){
            isActive = true;
        } else if (radioPast.isChecked()){
            isActive = false;
        } else {
            isActive  = fragmentId == TreatmentFragment.ID_FRAGMENT_CURRENT;
        }

        if (timeString.isEmpty()){
            timeInMillis = TreatmentEntity.TIME_NOT_SELECTED;
        } else {
            int time = Integer.valueOf(timeString);
            timeInMillis = getTimeInMillis(selectedTimeUnit, time);
        }

        if ((timeString.isEmpty()
                || selectedTimeUnit == TIME_UNIT_NOT_SELECTED)
                && name.isEmpty()){
            Toast.makeText(getContext(), getString(R.string.dialog_msg_provide_all_info), Toast.LENGTH_SHORT).show();
        } else {
            constructTreatment(symptomId, name, timeInMillis, treatmentSuccessInt, isActive);
            listener.onSaveTreatment(treatment, actionSaveOrEdit);
            dialog.dismiss();
        }
    }

    private void constructTreatment(int symptomId, String name, long timeInMillis,  int wasSuccessful,
                                     boolean isActive){
        if (treatment == null){
            treatment = new TreatmentEntity(symptomId, name, timeInMillis, wasSuccessful, isActive);
        } else {
            treatment.setName(name);
            treatment.setTakesEffectIn(timeInMillis);
            treatment.setWasSuccessful(wasSuccessful);
            treatment.setActive(isActive);
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

    private void setRadioSuccessId(int id){
        if (id == TreatmentEntity.WAS_SUCCESSFUL_NO){
            radioGroupSuccess.check(R.id.radioTreatmentUnsuccessful);
        } else if (id == TreatmentEntity.WAS_SUCCESSFUL_YES){
            radioGroupSuccess.check(R.id.radioTreatmentSuccessful);
        }
        treatmentSuccessInt = id;
    }

    private void setRadioIsActiveId(boolean isActive) {
        if (isActive){
            radioIsActive.check(R.id.radioTreatmentCurrent);
        } else {
            radioIsActive.check(R.id.radioTreatmentPast);
        }
    }
}
