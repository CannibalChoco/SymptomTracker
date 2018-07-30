package com.example.user.symptomtracker.ui.DialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.user.symptomtracker.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddNoteDialog extends DialogFragment {

    @BindView(R.id.editNewNote)
    EditText noteText;

    public AddNoteDialog() {
    }

    private OnSaveNote listener;

    public interface OnSaveNote{
        void onSaveNote(String note);
    }

    public void setOnSAveNoteListener(OnSaveNote listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.add_note_title)
                .setPositiveButton(R.string.action_save, (dialog, which) -> {
                    String note = noteText.getText().toString();
                    if (!note.isEmpty()){
                        listener.onSaveNote(note);
                    } else {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.action_discard, (dialog, which) -> dialog.dismiss());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_note, null);
        ButterKnife.bind(this, view);

        dialogBuilder.setView(view);
        return dialogBuilder.create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
