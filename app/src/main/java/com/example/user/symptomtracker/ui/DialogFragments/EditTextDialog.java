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

/**
 * Dialog fragment for:
 *     updating note;
 *     updating symptom name;
 *     adding new note;
 *
 * On save click, sends entered text back to activity through listener
 */
public class EditTextDialog extends DialogFragment {

    public static final String KEY_ID_EDIT_TYPE = "keyId";
    public static final String KEY_OLD_TEXT = "text";
    public static final String KEY_FRAGMENT_TITLE = "title";
    public static final int ID_NEW_NOTE = 0;
    public static final int ID_UPDATE_NOTE = 1;
    public static final int ID_UPDATE_NAME = 2;

    private int idEditType;
    private String text;

    @BindView(R.id.editNewNote)
    EditText noteText;

    public EditTextDialog() {
    }

    /**
     * Create DialogFragment instance with bundle
     * @param fragmentEditTypeId type Id for editing note or name, or adding a note
     * @param oldText text, if type is edit
     * @param listener dialog host
     * @return new EditTextDialog instance
     */
    public static EditTextDialog newInstance(int fragmentEditTypeId, String oldText, String title,
                                             OnSaveText listener){
        EditTextDialog fragment = new EditTextDialog();

        Bundle bundle = new Bundle();
        bundle.putInt(KEY_ID_EDIT_TYPE, fragmentEditTypeId);
        bundle.putString(EditTextDialog.KEY_OLD_TEXT, oldText);
        bundle.putString(KEY_FRAGMENT_TITLE, title);
        fragment.setArguments(bundle);

        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentWithTitle);
        fragment.setOnSaveNoteListener(listener);

        return fragment;
    }

    private OnSaveText listener;

    public interface OnSaveText {
        void onSaveText(int id, String text);
    }

    public void setOnSaveNoteListener(OnSaveText listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        text = args.getString(KEY_OLD_TEXT);
        idEditType = args.getInt(KEY_ID_EDIT_TYPE);
        String title = args.getString(KEY_FRAGMENT_TITLE);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setPositiveButton(R.string.action_save, (dialog, which) -> {
                    String note = noteText.getText().toString();
                    if (!note.isEmpty()){
                        listener.onSaveText(idEditType, note);
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

        if (!TextUtils.isEmpty(text)){
            noteText.setText(text);
            noteText.setSelection(text.length());
        }

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
