package com.example.user.symptomtracker.ui.DialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.user.symptomtracker.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditTextDialog extends DialogFragment {

    public static final String KEY_ID = "keyId";
    public static final String KEY_TEXT = "text";
    public static final int ID_NEW_NOTE = 0;
    public static final int ID_UPDATE_NOTE = 1;
    public static final int ID_UPDATE_NAME = 2;

    private int id;

    @BindView(R.id.editNewNote)
    EditText noteText;

    public EditTextDialog() {
    }

    private OnSaveText listener;

    public interface OnSaveText {
        void onSaveText(int id, String text);
    }

    public void setOnSAveNoteListener(OnSaveText listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                //TODO: Set the right name- new note, edit note, edit name
                .setTitle(R.string.add_note_title)
                .setPositiveButton(R.string.action_save, (dialog, which) -> {
                    String note = noteText.getText().toString();
                    if (!note.isEmpty()){
                        listener.onSaveText(id, note);
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

        Bundle bundle = getArguments();
        String text = bundle.getString(KEY_TEXT);
        id = bundle.getInt(KEY_ID);

        if (!TextUtils.isEmpty(text)){
            noteText.setText(text);
            noteText.setSelection(text.length());
        }

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
