package com.example.user.symptomtracker.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.example.user.symptomtracker.R;

public class UnsavedChangeDialogUtils {

    public static final int ACTION_DELETE_DIALOG = 0;
    public static final int ACTION_CANCEL_DIALOG = 1;

    /**
     * Construct an AlertDialog with message and actions
     * @param context Context that needs the AlertDialog
     * @param positiveButtonClickListener listener for positive button
     * @param dialogId ID for either Delete or Cancel dialog
     * @return AlertDialog with message and two possible actions
     */
    public static AlertDialog constructDialog(Context context, DialogInterface.OnClickListener
            positiveButtonClickListener, int dialogId){
        AlertDialog.Builder builder =
                new android.support.v7.app.AlertDialog.Builder(context);
        String message;
        String positive;
        String negative;
        if (dialogId == ACTION_DELETE_DIALOG){
            message = context.getString(R.string.dialog_msg_delete);
            positive = context.getString(R.string.action_delete);
            negative = context.getString(R.string.action_cancel);
        } else {
            message = context.getString(R.string.dialog_msg_unsaved_changes);
            positive = context.getString(R.string.action_discard);
            negative = context.getString(R.string.action_keep_editing);
        }
        builder.setMessage(message);
        builder.setPositiveButton(positive, positiveButtonClickListener);
        builder.setNegativeButton(negative, (dialog, id) -> {
            // User clicked the "Keep editing" button, so dismiss the dialog
            // and continue editing the symptom.
            if (dialog != null) {
                dialog.dismiss();
            }
        });

        // Create and show the AlertDialog
        return builder.create();
    }
}
